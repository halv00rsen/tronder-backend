package org.tronder.words.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Hallmark {

    @Id
    @NotNull
    private String hallmark;

    public Hallmark() {

    }

    public Hallmark(String hallmark) {
        this.hallmark = hallmark;
    }

    public String getHallmark() {
        return hallmark;
    }

    @Override
    public int hashCode() {
        return this.hallmark.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Hallmark) {
            return ((Hallmark) obj).hallmark.equals(this.hallmark);
        }
        return false;
    }

}
