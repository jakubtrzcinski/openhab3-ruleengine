package io.trzcinski.openhabruleengine.rule;

import io.trzcinski.openhabruleengine.condition.ConditionValidator;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
public class RuleValidator {
    public static void validate(Rule rule) {
        var condition = rule.when();
        try {
            ConditionValidator.validate(condition);
        } catch (ConditionValidator.ConditionValidatorException ex){
            throw new RuleValidatorException(String.format("Rule %s has illegal condition state", rule.name()), ex);
        }
    }

    public static class RuleValidatorException extends RuntimeException {
        public RuleValidatorException(String message) {
            super(message);
        }

        public RuleValidatorException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
