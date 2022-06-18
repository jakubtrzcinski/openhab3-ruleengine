package io.trzcinski.openhabruleengine.condition.evaluator;

import io.trzcinski.openhabclient.dto.Event;
import io.trzcinski.openhabruleengine.condition.Condition;
import io.trzcinski.openhabruleengine.condition.ItemStateChangedToCondition;
import io.trzcinski.openhabruleengine.condition.ItemStateIsCondition;
import io.trzcinski.openhabruleengine.item.ItemReference;
import io.trzcinski.openhabruleengine.item.ItemStateFacade;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import static io.trzcinski.openhabclient.dto.Event.Type.ItemStateEvent;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 15-06-2022
 */
@RequiredArgsConstructor
public class ItemStateIsConditionEvaluator implements ConditionEvaluator {

    private final ItemStateFacade itemStateFacade;

    @Override
    public boolean supports(Condition condition) {
        return condition instanceof ItemStateIsCondition;
    }

    @Override
    public boolean evaluate(Condition condition, Event event) {
        var item = ((ItemStateIsCondition)condition).getItem();
        var expectedValue = ((ItemStateIsCondition)condition).getExpectedValue();
        return compareValues(item, expectedValue);
    }

    protected boolean compareValues(Object value, Object expectedValue){
        if(expectedValue instanceof ItemReference){
            var propName = ((ItemReference) expectedValue).getName();
            expectedValue = itemStateFacade.getState(propName);
        }
        if(value instanceof ItemReference){
            var propName = ((ItemReference) value).getName();
            value = itemStateFacade.getState(propName);
        }
        return Objects.equals(expectedValue, value);
    }
}
