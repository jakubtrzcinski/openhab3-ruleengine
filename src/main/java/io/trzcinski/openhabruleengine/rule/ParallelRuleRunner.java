package io.trzcinski.openhabruleengine.rule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Slf4j
@RequiredArgsConstructor
public class ParallelRuleRunner implements RuleRunner {

    private final ExecutorService executorService;

    private final SimpleRuleRunner simpleRuleRunner;

    public ParallelRuleRunner() {
        this.executorService = Executors.newCachedThreadPool();
        this.simpleRuleRunner = new SimpleRuleRunner();
    }

    @Override
    public void run(Rule rule) {
        executorService.submit(()->{
            simpleRuleRunner.run(rule);
        });
    }
}
