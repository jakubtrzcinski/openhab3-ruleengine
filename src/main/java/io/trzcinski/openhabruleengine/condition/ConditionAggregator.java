package io.trzcinski.openhabruleengine.condition;

import lombok.Getter;

import java.util.List;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Getter
public abstract class ConditionAggregator extends Condition {

    protected final List<Condition> child;

    public ConditionAggregator(List<Condition> child) {
        if(child.size() <= 1){
            throw new IllegalArgumentException("ConditionAggregator should contain at least two elements");
        }
        this.child = child;
    }
}
