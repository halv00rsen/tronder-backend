package org.tronder.words.dataAccessObject;

import javax.validation.constraints.NotNull;

public class WordDTO {

    @NotNull
    private String wordText;
    @NotNull
    private String translation;
    private String description;

    public String getWordText() {
        return wordText;
    }

    public void setWordText(String wordText) {
        this.wordText = wordText;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WordDTO(@NotNull String wordText, @NotNull String translation, String description) {
        this.wordText = wordText;
        this.translation = translation;
        this.description = description;
    }

    public WordDTO() {

    }

}
