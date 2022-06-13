package io.trzcinski.openhabruleengine.rule;

import io.trzcinski.openhabclient.OpenhabClient;

/**
 * @author Jakub Trzcinski jakub@trzcinski.io
 * @since 13-06-2022
 */
public interface ModyfingRule extends Rule {
    void setOpenhabClient(OpenhabClient openhabClient);

}
