package org.tronder.words.model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Dialect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String displayName;

    @OneToMany
    private List<WordEntity> words;

    @NotNull
    private String createdBy;

    @Column(columnDefinition = "boolean default false")
    private boolean publicDialect;

    private String description;


    public Dialect(int id, @NotNull String displayName, List<WordEntity> words, @NotNull String createdBy, boolean publicDialect, String description) {
        this.id = id;
        this.displayName = displayName;
        this.words = words;
        this.createdBy = createdBy;
        this.publicDialect = publicDialect;
        this.description = description;
    }

    public Dialect() {

    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void addWord(WordEntity word) {
        this.words.add(word);
    }

    @JsonIgnore
    public List<WordEntity> getWords() {
        return words;
    }

    public void setWords(List<WordEntity> words) {
        this.words = words;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublicDialect() {
        return publicDialect;
    }

    public void setPublicDialect(boolean publicDialect) {
        this.publicDialect = publicDialect;
    }
}
