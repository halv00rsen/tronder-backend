package org.tronder.words.dataAccessObject;

import javax.validation.constraints.NotNull;

public class DialectDTO {

    @NotNull
    private String displayName;
    @NotNull
    private String description;

    private boolean publicDialect = false;

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isPublicDialect() {
        return publicDialect;
    }
}
