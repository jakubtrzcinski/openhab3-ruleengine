package io.trzcinski.openhabruleengine.condition.evaluator;

import io.trzcinski.openhabclient.dto.Event;
import io.trzcinski.openhabruleengine.condition.AndCondition;
import io.trzcinski.openhabruleengine.condition.Condition;
import io.trzcinski.openhabruleengine.condition.OrCondition;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 15-06-2022
 */
@AllArgsConstructor
@NoArgsConstructor
public class OrConditionEvaluator implements ConditionEvaluator {

    @Setter
    private RootConditionEvaluator root;

    @Override
    public boolean supports(Condition condition) {
        return condition instanceof OrCondition;
    }

    @Override
    public boolean evaluate(Condition condition, Event event) {
        var child = ((OrCondition)condition).getChild();
        return child.stream().anyMatch(it->root.evaluate(it, event));
    }
}
