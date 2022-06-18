package io.trzcinski.openhabruleengine.item;

import io.trzcinski.openhabclient.OpenhabClient;
import lombok.RequiredArgsConstructor;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 15-06-2022
 */
@RequiredArgsConstructor
public class ItemStateFacadeImpl implements ItemStateFacade {

    private final OpenhabClient openhabClient;

    @Override
    public void setState(String name, Object value) {
        openhabClient.item().setState(name, value.toString());

    }

    @Override
    public Object getState(String name) {
        return openhabClient.item().getState(name);
    }
}
