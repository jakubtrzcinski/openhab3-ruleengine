package io.trzcinski.openhabruleengine.condition;

import io.trzcinski.openhabclient.dto.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static io.trzcinski.openhabclient.dto.Event.Type.ItemStateEvent;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Getter
@RequiredArgsConstructor
public class ItemStateChangedCondition extends Condition {
    private final String itemName;

    @Override
    public boolean evaluate(Event event) {
        return event.getTopic().contains("items/"+itemName) && event.getType() == ItemStateEvent;
    }
}
