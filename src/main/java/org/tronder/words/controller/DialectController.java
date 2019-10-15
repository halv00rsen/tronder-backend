package org.tronder.words.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tronder.words.dataAccessObject.DialectDTO;
import org.tronder.words.dataAccessObject.WordDTO;
import org.tronder.words.model.Dialect;
import org.tronder.words.model.UserData;
import org.tronder.words.model.WordEntity;
import org.tronder.words.service.DialectService;

@RestController
@RequestMapping("/dialect")
public class DialectController {

    private final DialectService dialectService;

    @Autowired
    public DialectController(DialectService dialectService) {
        this.dialectService = dialectService;
    }


    @GetMapping("")
    public Iterable<Dialect> getAllDialects(Authentication auth) {
        System.out.println(getUserData(auth).getEmail());
        System.out.println(getUserData(auth).getSub());
        System.out.println(getUserData(auth).getName());
        return dialectService.getDialects();
    }

    @PostMapping("")
    public Dialect addDialect(@RequestBody DialectDTO dialectDTO) {
        Dialect dialect = new Dialect();
        dialect.setDescription(dialectDTO.getDescription());
        dialect.setDisplayName(dialectDTO.getDisplayName());
        return dialectService.addDialect(dialect);
    }

    @GetMapping("/{dialectId}/word")
    public List<WordEntity> getWordsInDialect(@PathVariable int dialectId) {
        return dialectService.getWordsInDialect(dialectId);
    }

    @PostMapping("/{dialectId}/word")
    public WordEntity addNewWordToDialect(@PathVariable int dialectId, @RequestBody WordDTO word) {
        WordEntity wordEntity = new WordEntity();
        wordEntity.setDescription(word.getDescription());
        wordEntity.setTranslation(word.getTranslation());
        wordEntity.setWordText(word.getWordText());
        return dialectService.addWordToDialect(dialectId, wordEntity);
    }

    private UserData getUserData(Authentication auth) {
        return (UserData) auth.getPrincipal();
    }

}
