package io.trzcinski.openhabruleengine.condition.evaluator;

import io.trzcinski.openhabclient.dto.Event;
import io.trzcinski.openhabruleengine.condition.Condition;
import io.trzcinski.openhabruleengine.condition.ItemStateChangedCondition;
import io.trzcinski.openhabruleengine.condition.SystemCondition;

import static io.trzcinski.openhabclient.dto.Event.Type.ItemStateEvent;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 15-06-2022
 */
public class SystemConditionEvaluator implements ConditionEvaluator {

    @Override
    public boolean supports(Condition condition) {
        return condition instanceof SystemCondition;
    }

    @Override
    public boolean evaluate(Condition condition, Event event) {
        var eventType = ((SystemCondition)condition).getEventType();
        return event.getTopic().equals(eventType);
    }
}

