package org.tronder.words.dataAccessObject;

import javax.validation.constraints.NotNull;
import java.util.List;

public class DialectDTO {

    @NotNull
    private String displayName;
    @NotNull
    private String description;

    private boolean publicDialect = false;

    private List<String> hallmarks;

    public DialectDTO() {

    }

    public DialectDTO(@NotNull String displayName, @NotNull String description, boolean publicDialect, List<String> hallmarks) {
        this.displayName = displayName;
        this.description = description;
        this.publicDialect = publicDialect;
        this.hallmarks = hallmarks;
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

    public List<String> getHallmarks() {
        return hallmarks;
    }
}
