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
import io.trzcinski.openhabruleengine.rule.Rule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
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
public class TimeInvocationSource implements RuleInvocationSource {

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

    public TimeInvocationSource(OpenhabClient client, Collection<Rule> rules) {
        this.client = client;
        this.rules = rules;


        for (Rule rule : rules) {
            var crons = findCrons(rule.when());

            crons.forEach(cron->{
                executor.scheduleAtFixedRate(rule::run,0, invokeInSeconds(cron), TimeUnit.SECONDS);
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
        return null;
    }


    private Long invokeInSeconds(String cron){
        Cron quartzCron = parser.parse(cron);
        return ExecutionTime.forCron(quartzCron)
                .timeToNextExecution(ZonedDateTime.now())
                .map(Duration::getSeconds)
                .orElse(null);
    }

    @Override
    public Thread listen(Consumer<Rule> toRun) {
        var eventClient = client.event();

        eventClient.all().subscribe(it->{
            matchedRules(it).forEach(toRun);
        });
        return eventClient.getListener();
    }

    private List<Rule> matchedRules(Event event){
        var ret = new ArrayList<Rule>();
        for (Rule rule : rules) {
            if(rule.when().evaluate(event)){
                log.debug(String.format("Matched Rule %s with event %s", rule.name(), event));
                ret.add(rule);
            }
        }
        return ret;
    }
}
