package io.trzcinski.openhabruleengine.condition;

import io.trzcinski.openhabclient.dto.Event;

import java.time.LocalDateTime;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
public abstract class Condition {

    public abstract boolean evaluate(Event event);

    public Condition and(Condition condition){
        return ConditionBuilder.and(this, condition);
    }

    public Condition or(Condition condition){
        return ConditionBuilder.and(this, condition);
    }

}
