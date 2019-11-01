package org.tronder.words.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.tronder.words.dataAccessObject.UserProviderDTO;
import org.tronder.words.errors.BadRequestException;

@Entity
public class Dialect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String displayName;

    @OneToMany
    private List<WordEntity> words = new ArrayList<>();

    @NotNull
    private String createdBy;

    @Column(columnDefinition = "boolean default false")
    private boolean publicDialect;

    private String description;

    @ManyToMany
    private Set<Hallmark> hallmarks = new HashSet<>();

    @Transient
    private UserProviderDTO userProviderDTO;


    public Dialect(int id, @NotNull String displayName, List<WordEntity> words, @NotNull String createdBy, boolean publicDialect, String description, Set<Hallmark> hallmarks) {
        this.id = id;
        this.words = words;
        this.createdBy = createdBy;
        this.publicDialect = publicDialect;
        this.hallmarks = hallmarks;
        setDisplayName(displayName);
        setDescription(description);
    }

    public Dialect() {

    }

    @JsonIgnore
    public Set<Hallmark> getHallmarksSet() {
        return hallmarks;
    }

    @JsonSerialize
    public List<String> getHallmarks() {
        List<String> hallmarks = new ArrayList<>();
        this.hallmarks.stream().forEach(hallmark -> hallmarks.add(hallmark.getHallmark()));
        return hallmarks;
    }

    public void setHallmarks(Set<Hallmark> hallmarks) {
        this.hallmarks = hallmarks;
    }

    @JsonSerialize
    private UserProviderDTO getUserInfo() {
        return userProviderDTO;
    }

    @JsonIgnore
    public void setUserProviderDTO(UserProviderDTO userProviderDTO) {
        this.userProviderDTO = userProviderDTO;
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
        displayName = displayName.trim();
        if (displayName.length() == 0) {
            throw new BadRequestException("Display name for dialect must not be zero");
        }
        this.displayName = displayName;
    }

    public void addWord(WordEntity word) {
        this.words.add(word);
    }

    @JsonSerialize
    public int numWords() {
        return words.size();
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
        this.description = description.trim();
    }

    public boolean isPublicDialect() {
        return publicDialect;
    }

    public void setPublicDialect(boolean publicDialect) {
        this.publicDialect = publicDialect;
    }
}
