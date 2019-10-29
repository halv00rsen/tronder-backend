package org.tronder.words.model;

import javax.validation.constraints.NotNull;

public class UserData {

    @NotNull
    private String email;
    @NotNull
    private String name;
    @NotNull
    private String sub;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
}
