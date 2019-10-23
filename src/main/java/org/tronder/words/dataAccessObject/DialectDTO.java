package org.tronder.words.dataAccessObject;

import javax.validation.constraints.NotNull;

public class DialectDTO {

    @NotNull
    private String displayName;
    @NotNull
    private String description;

    private boolean publicDialect = false;

    public DialectDTO() {

    }

    public DialectDTO(@NotNull String displayName, @NotNull String description, boolean publicDialect) {
        this.displayName = displayName;
        this.description = description;
        this.publicDialect = publicDialect;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

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
