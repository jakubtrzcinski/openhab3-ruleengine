package io.trzcinski.openhabruleengine.condition.evaluator;

import io.trzcinski.openhabclient.dto.Event;
import io.trzcinski.openhabruleengine.condition.Condition;
import io.trzcinski.openhabruleengine.item.ItemStateFacade;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 15-06-2022
 */

public class RootConditionEvaluator implements ConditionEvaluator {

    private final List<ConditionEvaluator> conditionEvaluators;

    public RootConditionEvaluator(ItemStateFacade itemStateFacade) {
        conditionEvaluators = List.of(
                new AndConditionEvaluator(this),
                new OrConditionEvaluator(this),
                new ItemStateChangedConditionEvaluator(),
                new ItemStateChangedToConditionEvaluator(itemStateFacade),
                new ItemStateIsConditionEvaluator(itemStateFacade),
                new SystemConditionEvaluator()
        );
    }

    @Override
    public boolean supports(Condition condition) {
        return true;
    }

    @Override
    public boolean evaluate(Condition condition, Event event) {
        if(!event.getTopic().contains("item") && !event.getTopic().contains("internal")
        ){
            return false;
        }
        for (ConditionEvaluator conditionEvaluator : conditionEvaluators) {
            if(conditionEvaluator.supports(condition)){
                return conditionEvaluator.evaluate(condition, event);
            }
        }
        return false;
    }
}
