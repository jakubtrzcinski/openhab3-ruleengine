package io.trzcinski.openhabruleengine.ruleinvocation;

import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import io.trzcinski.openhabclient.OpenhabClient;
import io.trzcinski.openhabclient.dto.Event;
import io.trzcinski.openhabruleengine.condition.Condition;
import io.trzcinski.openhabruleengine.condition.CronCondition;
import io.trzcinski.openhabruleengine.condition.OrCondition;
import io.trzcinski.openhabruleengine.condition.evaluator.ConditionEvaluator;
import io.trzcinski.openhabruleengine.condition.evaluator.RootConditionEvaluator;
import io.trzcinski.openhabruleengine.item.ItemStateFacadeImpl;
import io.trzcinski.openhabruleengine.rule.Rule;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Slf4j
public class CronInvocationSource implements RuleInvocationSource {

    private final OpenhabClient client;

    private final Collection<Rule> rules;

    private final CronDefinition cronDefinition =
            CronDefinitionBuilder.defineCron()
                    .withSeconds().and()
                    .withMinutes().and()
                    .withHours().and()
                    .withDayOfMonth()
                    .supportsHash().supportsL().supportsW().supportsQuestionMark().and()
                    .withMonth().and()
                    .withDayOfWeek()
                    .withIntMapping(7, 0)
                    .supportsHash().supportsL().supportsW().and()
                    .withYear().optional().and()
                    .instance();

    private final CronParser parser = new CronParser(cronDefinition);

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private final ConditionEvaluator conditionEvaluator;

    public CronInvocationSource(OpenhabClient client, Collection<Rule> rules) {
        this.client = client;
        this.rules = rules;

        this.conditionEvaluator = new RootConditionEvaluator(new ItemStateFacadeImpl(client));

        for (Rule rule : rules) {
            var crons = findCrons(rule.when());

            crons.forEach(cron->{
                var executeIn = invokeInSeconds(cron);
                var now = ZonedDateTime.now();
                var initDelay = executeIn.timeToNextExecution(now).map(Duration::getSeconds).orElse(0L);
                var fromLast = executeIn.timeFromLastExecution(now).map(Duration::getSeconds).orElse(0L);
                executor.scheduleAtFixedRate(
                        rule::run,
                        initDelay,
                        initDelay+fromLast,
                        TimeUnit.SECONDS
                );
            });
        }
    }

    private List<String> findCrons(Condition rule) {
        if(rule instanceof CronCondition){
            return List.of(((CronCondition) rule).getExpr());
        }
        if(rule instanceof OrCondition){
            return ((OrCondition) rule).getChild().stream()
                    .map(this::findCrons)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    private ExecutionTime invokeInSeconds(String cron){
        Cron quartzCron = parser.parse(cron);
        return ExecutionTime.forCron(quartzCron);
    }

    @Override
    public Thread listen() {
        return null;
    }
}
