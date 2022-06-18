package io.trzcinski.openhabruleengine.ruleinvocation;

import io.trzcinski.openhabclient.OpenhabClient;
import io.trzcinski.openhabclient.dto.Event;
import io.trzcinski.openhabruleengine.condition.evaluator.ConditionEvaluator;
import io.trzcinski.openhabruleengine.condition.evaluator.RootConditionEvaluator;
import io.trzcinski.openhabruleengine.item.ItemStateFacadeImpl;
import io.trzcinski.openhabruleengine.rule.Rule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
@Slf4j
@RequiredArgsConstructor
public class EventRuleInvocationSource implements RuleInvocationSource {

    private final OpenhabClient client;

    private final Collection<Rule> rules;

    private final ConditionEvaluator conditionEvaluator;

    public EventRuleInvocationSource(OpenhabClient client, Collection<Rule> rules) {
        this.client = client;
        this.rules = rules;
        this.conditionEvaluator = new RootConditionEvaluator(new ItemStateFacadeImpl(client));
    }

    @Override
    public Thread listen() {
        var eventClient = client.event();

        matchedRules(new Event("internal/startup", null, null)).forEach(Rule::run);

        eventClient.all().subscribe(it-> matchedRules(it).forEach(Rule::run));

        var thread = eventClient.getListener();

        thread.setName("EventRuleInvocationSource");
        return thread;
    }

    private List<Rule> matchedRules(Event event){
        var ret = new ArrayList<Rule>();
        for (Rule rule : rules) {

            if(conditionEvaluator.evaluate(rule.when(), event)){
                log.debug(String.format("Matched Rule %s with event %s", rule.name(), event));
                ret.add(rule);
            }
        }
        return ret;
    }
}
