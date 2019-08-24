package org.tronder.words.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class WordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String wordText;

    public WordEntity() {

    }

    public WordEntity(int id, String wordText) {
        this.id = id;
        this.wordText = wordText;
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

}
