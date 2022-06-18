package io.trzcinski.openhabruleengine.item;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 15-06-2022
 */
public interface ItemStateFacade {
    void setState(String name, Object value);

    Object getState(String name);

    default Double getNumber(String name){
        return Double.parseDouble(getState(name).toString());
    }
}
