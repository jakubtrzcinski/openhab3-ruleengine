package io.trzcinski.openhabruleengine.condition;

import io.trzcinski.openhabclient.dto.Event;
import lombok.Getter;

import java.util.List;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Getter
public class OrCondition extends ConditionAggregator {

    public OrCondition(List<Condition> child) {
        super(child);
    }

}
