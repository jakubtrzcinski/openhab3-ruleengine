package io.trzcinski.openhabruleengine.rule;

import io.trzcinski.openhabruleengine.condition.Condition;
import io.trzcinski.openhabruleengine.item.ItemStateFacade;
import lombok.Setter;
import lombok.experimental.Delegate;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
public abstract class Rule {
    public static String ON = "ON";
    public static String OFF = "OFF";

    @Delegate
    @Setter
    private ItemStateFacade itemStateFacade;

    public String name() {
        return this.getClass().getName();
    }
    public abstract Condition when();
    public abstract void run();
}
