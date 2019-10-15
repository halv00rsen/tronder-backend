package org.tronder.words.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    private String description;

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

    public Dialect(int id, @NotNull String displayName, List<WordEntity> words, String description) {
        this.id = id;
        this.displayName = displayName;
        this.words = words;
        this.description = description;
    }

    public Dialect() {

    }

}
