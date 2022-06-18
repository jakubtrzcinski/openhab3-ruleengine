package io.trzcinski.openhabruleengine.condition;

import io.trzcinski.openhabclient.dto.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import static io.trzcinski.openhabclient.dto.Event.Type.ItemStateEvent;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Getter
@RequiredArgsConstructor
public class ItemStateChangedToCondition extends Condition {
    private final String itemName;
    private final Object expectedValue;
}
