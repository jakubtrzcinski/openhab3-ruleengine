package io.trzcinski.openhabruleengine.rule;

import io.trzcinski.openhabruleengine.condition.Condition;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
public interface Rule {
    default String name() {
        return this.getClass().getName();
    }
    public Condition when();
    void run();
}
