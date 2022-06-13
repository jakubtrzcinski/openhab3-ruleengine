package io.trzcinski.openhabruleengine.condition;

import io.trzcinski.openhabclient.OpenhabClient;
import io.trzcinski.openhabclient.dto.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

import static io.trzcinski.openhabclient.dto.Event.Type.ItemStateEvent;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Getter
@RequiredArgsConstructor
public class ItemStateIsCondition extends ReadingStateCondition {
    private final String itemName;
    private final Object expectedValue;
    @Setter
    private OpenhabClient openhabClient;

    @Override
    public boolean evaluate(Event event) {
        return event.getTopic().contains("items/"+itemName)
                && event.getType() == ItemStateEvent
                && Objects.equals(openhabClient.item().getState(itemName), expectedValue);
    }
}
