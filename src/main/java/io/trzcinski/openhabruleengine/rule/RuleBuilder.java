package io.trzcinski.openhabruleengine.rule;

import io.trzcinski.openhabruleengine.condition.Condition;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 15-06-2022
 */
public class RuleBuilder {
    private String name;
    private Condition when;

    public static RuleBuilder rule(String name) {
        var self = new RuleBuilder();
        self.name = name;
        return self;
    }
    public RuleBuilder when(Condition when){
        this.when = when;
        return this;
    }
    public Rule then(Runnable then){
        return new Rule() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public Condition when() {
                return when;
            }

            @Override
            public void run() {
                then.run();
            }
        };
    }
}
