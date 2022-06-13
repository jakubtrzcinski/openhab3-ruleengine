package io.trzcinski.openhabruleengine.condition;

import io.trzcinski.openhabclient.dto.Event;
import lombok.Getter;

import java.util.List;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Getter
public class AndCondition extends ConditionAggregator {

    public AndCondition(List<Condition> child) {
        super(child);
    }

    @Override
    public boolean evaluate(Event event) {
        return child.stream().allMatch(it->evaluate(event));
    }
}
