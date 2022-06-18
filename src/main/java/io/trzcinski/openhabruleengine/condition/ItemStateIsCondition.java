package io.trzcinski.openhabruleengine.condition;

import io.trzcinski.openhabclient.OpenhabClient;
import io.trzcinski.openhabclient.dto.Event;
import io.trzcinski.openhabruleengine.item.ItemReference;
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
public class ItemStateIsCondition extends Condition {
    private final ItemReference item;
    private final Object expectedValue;

}
