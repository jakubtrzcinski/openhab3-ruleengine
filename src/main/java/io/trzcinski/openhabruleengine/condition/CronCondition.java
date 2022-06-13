package io.trzcinski.openhabruleengine.condition;

import io.trzcinski.openhabclient.dto.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Time;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Getter
@RequiredArgsConstructor
public class CronCondition extends Condition {

    private final String expr;

    @Override
    public boolean evaluate(Event event) {
        return false;
    }
}
