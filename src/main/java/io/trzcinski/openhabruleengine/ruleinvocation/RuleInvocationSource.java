package io.trzcinski.openhabruleengine.ruleinvocation;

import io.trzcinski.openhabruleengine.rule.Rule;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
public interface RuleInvocationSource {
    Thread listen(Consumer<Rule> toRun);
}
