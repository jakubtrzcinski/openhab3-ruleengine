package io.trzcinski.openhabruleengine.condition.evaluator;

import io.trzcinski.openhabclient.dto.Event;
import io.trzcinski.openhabruleengine.condition.Condition;
import io.trzcinski.openhabruleengine.condition.ItemStateChangedCondition;
import io.trzcinski.openhabruleengine.condition.ItemStateChangedToCondition;
import io.trzcinski.openhabruleengine.item.ItemReference;
import io.trzcinski.openhabruleengine.item.ItemStateFacade;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import static io.trzcinski.openhabclient.dto.Event.Type.ItemCommandEvent;
import static io.trzcinski.openhabclient.dto.Event.Type.ItemStateEvent;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 15-06-2022
 */
@RequiredArgsConstructor
public class ItemStateChangedToConditionEvaluator implements ConditionEvaluator {

    private final ItemStateFacade facade;

    @Override
    public boolean supports(Condition condition) {
        return condition instanceof ItemStateChangedToCondition;
    }

    @Override
    public boolean evaluate(Condition condition, Event event) {
        var itemName = ((ItemStateChangedToCondition)condition).getItemName();
        var expectedValue = ((ItemStateChangedToCondition)condition).getExpectedValue();
        return event.getTopic().contains("items/"+itemName+"/")
                && compareValues(event.getPayload().getValue(), expectedValue)
                && (event.getType() == ItemStateEvent || event.getType() == ItemCommandEvent);
    }

    protected boolean compareValues(Object value, Object expectedValue){
        if(expectedValue instanceof ItemReference){
            var propName = ((ItemReference) expectedValue).getName();
            expectedValue = facade.getState(propName);
        }
        return Objects.equals(expectedValue, value);
    }
}
