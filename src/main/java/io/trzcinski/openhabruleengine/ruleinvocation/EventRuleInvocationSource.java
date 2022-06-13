package io.trzcinski.openhabruleengine.ruleinvocation;

import io.trzcinski.openhabclient.OpenhabClient;
import io.trzcinski.openhabclient.dto.Event;
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

    @Override
    public Thread listen(Consumer<Rule> toRun) {
        var eventClient = client.event();

        eventClient.all().subscribe(it->{
            matchedRules(it).forEach(toRun);
        });
        return eventClient.getListener();
    }

    private List<Rule> matchedRules(Event event){
        var ret = new ArrayList<Rule>();
        for (Rule rule : rules) {
            if(rule.when().evaluate(event)){
                log.debug(String.format("Matched Rule %s with event %s", rule.name(), event));
                ret.add(rule);
            }
        }
        return ret;
    }
}
