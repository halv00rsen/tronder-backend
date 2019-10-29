package org.tronder.words.controller;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tronder.words.dataAccessObject.DialectDTO;
import org.tronder.words.dataAccessObject.WordDTO;
import org.tronder.words.model.Dialect;
import org.tronder.words.model.Hallmark;
import org.tronder.words.model.WordEntity;
import org.tronder.words.service.DialectService;
import org.tronder.words.utils.AuthenticationUtil;

@RestController
@RequestMapping("/dialect")
public class DialectController implements Serializable {

    private final DialectService dialectService;

    @Autowired
    public DialectController(DialectService dialectService) {
        this.dialectService = dialectService;
    }


    @GetMapping("")
    public Iterable<Dialect> getAllDialects(AuthenticationUtil auth) {
        if (auth.isAuthenticated()) {
            return dialectService.getPublicAndUserDialects(auth.getUserData().getSub());
        } else {
            return dialectService.getPublicDialects();
        }
    }

    @PostMapping("")
    public Dialect addDialect(@RequestBody DialectDTO dialectDTO, AuthenticationUtil auth) {
        Dialect dialect = new Dialect();
        dialect.setDescription(dialectDTO.getDescription());
        dialect.setDisplayName(dialectDTO.getDisplayName());
        dialect.setPublicDialect(dialectDTO.isPublicDialect());
        dialect.setCreatedBy(auth.getUserData().getSub());
        dialect.setHallmarks(deserializeHallmarks(dialectDTO.getHallmarks()));
        return dialectService.addDialect(dialect);
    }

    @GetMapping("/{dialectId}/word")
    public List<WordEntity> getWordsInDialect(@PathVariable int dialectId, AuthenticationUtil auth) {
        String userSub = "";
        if (auth.isAuthenticated()) {
            userSub = auth.getUserData().getSub();
        }
        return dialectService.getWordsInDialect(dialectId, userSub);
    }

    @PostMapping("/{dialectId}/word")
    public WordEntity addNewWordToDialect(@PathVariable int dialectId, @RequestBody WordDTO word, AuthenticationUtil auth) {
        WordEntity wordEntity = new WordEntity();
        wordEntity.setDescription(word.getDescription());
        wordEntity.setTranslation(word.getTranslation());
        wordEntity.setWordText(word.getWordText());
        return dialectService.addWordToDialect(dialectId, wordEntity, auth.getUserData().getSub());
    }

    private Set<Hallmark> deserializeHallmarks(List<String> hallmarks) {
        HashSet<Hallmark> hallmarkSet = new HashSet<>();
        for (String hallmark : hallmarks) {
            hallmarkSet.add(new Hallmark(hallmark));
        }
        return hallmarkSet;
    }

}
