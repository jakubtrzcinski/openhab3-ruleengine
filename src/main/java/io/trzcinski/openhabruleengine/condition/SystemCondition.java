package io.trzcinski.openhabruleengine.condition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Getter
@RequiredArgsConstructor
public class SystemCondition extends Condition {
    private final String eventType;
}
