package io.trzcinski.openhabruleengine.condition.evaluator;

import io.trzcinski.openhabclient.dto.Event;
import io.trzcinski.openhabruleengine.condition.Condition;
import io.trzcinski.openhabruleengine.condition.ItemStateChangedCondition;
import io.trzcinski.openhabruleengine.condition.OrCondition;
import lombok.Setter;

import static io.trzcinski.openhabclient.dto.Event.Type.ItemCommandEvent;
import static io.trzcinski.openhabclient.dto.Event.Type.ItemStateEvent;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 15-06-2022
 */
public class ItemStateChangedConditionEvaluator implements ConditionEvaluator {

    @Override
    public boolean supports(Condition condition) {
        return condition instanceof ItemStateChangedCondition;
    }

    @Override
    public boolean evaluate(Condition condition, Event event) {
        var itemName = ((ItemStateChangedCondition)condition).getItemName();
        return event.getTopic().contains("items/"+itemName+"/") && (event.getType() == ItemStateEvent || event.getType() == ItemCommandEvent);
    }
}

