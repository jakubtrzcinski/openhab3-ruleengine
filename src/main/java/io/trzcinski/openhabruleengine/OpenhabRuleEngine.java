package io.trzcinski.openhabruleengine;

import io.trzcinski.openhabclient.OpenhabClient;
import io.trzcinski.openhabclient.dto.Event;
import io.trzcinski.openhabruleengine.condition.Condition;
import io.trzcinski.openhabruleengine.condition.ConditionAggregator;
import io.trzcinski.openhabruleengine.condition.ReadingStateCondition;
import io.trzcinski.openhabruleengine.rule.*;
import io.trzcinski.openhabruleengine.ruleinvocation.EventRuleInvocationSource;
import io.trzcinski.openhabruleengine.ruleinvocation.RuleInvocationSource;
import io.trzcinski.openhabruleengine.ruleinvocation.TimeInvocationSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Slf4j
@RequiredArgsConstructor
public class OpenhabRuleEngine {

    private final OpenhabClient client;

    private final Set<Rule> rules;

    private final RuleRunner ruleRunner;

    private final Set<RuleInvocationSource> ruleInvocationSources = new HashSet<>();


    public OpenhabRuleEngine(OpenhabClient client, Collection<Rule> rules) {
        this.client = client;
        this.rules = Set.copyOf(rules);
        this.ruleRunner = new ParallelRuleRunner();
        init();
    }

    public Thread start() {
        var thread = new Thread(this::doStart);
        thread.start();
        return thread;
    }

    private void init(){
        log.info(String.format("Using %s rule runner", ruleRunner.getClass()));
        for (Rule rule : rules) {
            RuleValidator.validate(rule);
            log.info(String.format("Registered rule %s", rule.name()));
            if(rule instanceof ModyfingRule){
                ((ModyfingRule) rule).setOpenhabClient(client);
            }
            enrichConditions(rule.when());
        }
        addInvocationSource(new EventRuleInvocationSource(client, rules));
        addInvocationSource(new TimeInvocationSource(client, rules));
    }

    private void enrichConditions(Condition condition){
        if(condition instanceof ReadingStateCondition){
            ((ReadingStateCondition) condition).setOpenhabClient(client);
        }
        if(condition instanceof ConditionAggregator){
            ((ConditionAggregator) condition).getChild().forEach(this::enrichConditions);
        }
    }

    private void addInvocationSource(RuleInvocationSource ruleInvocationSource){
        log.info(String.format("Registered invocation source %s", ruleInvocationSource.getClass()));
        ruleInvocationSources.add(ruleInvocationSource);
    }


    private void doStart() {
        ruleInvocationSources
            .stream()
            .map(it -> it.listen(ruleRunner::run))
            .forEach(it->{
                try {
                    it.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
    }
}
