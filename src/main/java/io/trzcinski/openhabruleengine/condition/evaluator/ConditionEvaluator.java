package io.trzcinski.openhabruleengine.condition.evaluator;

import io.trzcinski.openhabclient.dto.Event;
import io.trzcinski.openhabruleengine.condition.Condition;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 15-06-2022
 */
public interface ConditionEvaluator {

    boolean supports(Condition condition);

    boolean evaluate(Condition condition, Event event);
}
