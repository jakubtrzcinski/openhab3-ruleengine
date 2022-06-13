package io.trzcinski.openhabruleengine.rule;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Slf4j
public class SimpleRuleRunner implements RuleRunner {

    private final List<String> runningRules = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void run(Rule rule) {
        long start = System.currentTimeMillis();
        if(runningRules.contains(rule.name())){
            log.warn(String.format("Rule %s is already running, execution ignored", rule.name()));
            return;
        }
        runningRules.add(rule.name());
        try {
            rule.run();
            long time = System.currentTimeMillis() - start;
            log.info(String.format("Rule %s executed in %d ms", rule.name(), time));
            if(time > 5000){
                log.warn("Rule %s took more than 5000ms to be executed, consider to optimise it.");
            }
        } catch (Exception ex){
            long time = System.currentTimeMillis() - start;
            log.error(String.format("Rule %s has failed in %d ms", rule.name(), time), ex);
        } finally {
            runningRules.remove(rule.name());
        }
    }
}
