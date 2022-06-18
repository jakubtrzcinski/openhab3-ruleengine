package io.trzcinski.openhabruleengine.condition.evaluator;

import io.trzcinski.openhabclient.dto.Event;
import io.trzcinski.openhabruleengine.condition.AndCondition;
import io.trzcinski.openhabruleengine.condition.Condition;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 15-06-2022
 */
@AllArgsConstructor
@NoArgsConstructor
public class AndConditionEvaluator implements ConditionEvaluator {

    @Setter
    private RootConditionEvaluator root;



    @Override
    public boolean supports(Condition condition) {
        return condition instanceof AndCondition;
    }

    @Override
    public boolean evaluate(Condition condition, Event event) {
        var child = ((AndCondition)condition).getChild();
        return child.stream().allMatch(it->root.evaluate(it, event));
    }
}
