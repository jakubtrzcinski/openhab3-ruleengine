package io.trzcinski.openhabruleengine.condition;

import io.trzcinski.openhabclient.OpenhabClient;
import io.trzcinski.openhabclient.dto.Event;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
public abstract class ReadingStateCondition extends Condition  {
    public abstract void setOpenhabClient(OpenhabClient openhabClient);
}
