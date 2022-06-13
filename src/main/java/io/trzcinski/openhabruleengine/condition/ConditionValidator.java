package io.trzcinski.openhabruleengine.condition;

import java.util.List;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
public class ConditionValidator {
    private static List<Class> timedRules = List.of(CronCondition.class);
    private static List<Class> eventRules = List.of(
            ItemStateChangedToCondition.class,
            ItemStateChangedCondition.class
    );
    private static List<Class> stateRules = List.of(
            ItemStateIsCondition.class
    );

    public static void validate(Condition condition) {
        if(condition instanceof AndCondition){
            var child = ((AndCondition) condition).getChild();
            var hasTimed = listContainsClassesOfInstances(
                    child,
                    timedRules
            );
            var hasEvent = listContainsClassesOfInstances(
                    child,
                    eventRules
            );
            var hasState = listContainsClassesOfInstances(
                    child,
                    stateRules
            );

            if(hasTimed && hasEvent){
                throw new ConditionValidatorException("And condition cannot has both Time condition and Event condition. ");
            }
            if(!hasTimed && !hasEvent && hasState){
                throw new ConditionValidatorException("And condition cannot has only state checking.");
            }
            child.forEach(ConditionValidator::validate);
        }
    }


    public static class ConditionValidatorException extends RuntimeException {
        public ConditionValidatorException(String message) {
            super(message);
        }
    }
    private static boolean listContainsClassesOfInstances(List<Condition> values, List<Class> allowed){
        for (Object value : values) {
            if(allowed.contains(value.getClass())){
                return true;
            }
        }
        return false;
    }
    private static boolean listContainsOnlyClassesOfInstances(List<Condition> values, List<Class> allowed){
        for (Object value : values) {
            if(!allowed.contains(value.getClass())){
                return false;
            }
        }
        return true;
    }
}
