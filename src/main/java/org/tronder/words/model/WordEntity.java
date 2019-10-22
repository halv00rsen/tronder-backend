package org.tronder.words.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class WordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String wordText;
    @NotNull
    private String translation;
    private String description;

    public WordEntity() {

    }

    public WordEntity(int id, String wordText, String translation) {
        this.id = id;
        this.wordText = wordText;
        this.translation = translation;
    }

    public String getWordText() {
        return wordText;
    }

    public void setWordText(String wordText) {
        if (wordText.length() == 0) {
            throw new IllegalArgumentException("The word must not be empty.");
        }
        this.wordText = wordText;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTranslation() {
        return translation;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
