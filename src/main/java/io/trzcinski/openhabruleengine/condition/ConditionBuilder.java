package io.trzcinski.openhabruleengine.condition;

import io.trzcinski.openhabruleengine.item.ItemReference;

import java.util.List;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
public class ConditionBuilder {

    public static Condition and(Condition... conditions) {
        return new AndCondition(List.of(conditions));
    }

    public static Condition and(List<Condition> conditions) {
        return new AndCondition(conditions);
    }

    public static Condition allOf(Condition... conditions) {
        return new AndCondition(List.of(conditions));
    }

    public static Condition allOf(List<Condition> conditions) {
        return new AndCondition(conditions);
    }


    public static Condition or(Condition... conditions) {
        return new OrCondition(List.of(conditions));
    }

    public static Condition or(List<Condition> conditions) {
        return new OrCondition(conditions);
    }

    public static Condition anyOf(Condition... conditions) {
        return new OrCondition(List.of(conditions));
    }

    public static Condition anyOf(List<Condition> conditions) {
        return new OrCondition(conditions);
    }
    public static Condition cron(String expr) {
        return new CronCondition(expr);
    }

    public static Condition itemChanged(String itemName) {
        return new ItemStateChangedCondition(itemName);
    }
    public static Condition onStartup() {
        return new SystemCondition("internal/startup");
    }

    public static Condition itemChangedTo(String itemName, Object expectedValue) {
        return new ItemStateChangedToCondition(itemName, expectedValue);
    }
    public static Condition itemHasValue(String itemName, Object expectedValue) {
        return new ItemStateIsCondition(new ItemReference(itemName), expectedValue);
    }
    public static Condition itemHasValue(ItemReference itemName, Object expectedValue) {
        return new ItemStateIsCondition(itemName, expectedValue);
    }
    public static ItemReference item(String itemName) {
        return new ItemReference(itemName);
    }
}
