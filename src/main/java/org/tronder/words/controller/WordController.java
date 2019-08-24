package org.tronder.words.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tronder.words.model.WordEntity;
import org.tronder.words.repository.WordRepository;

@RestController
@RequestMapping("/word")
public class WordController {

    @Autowired
    private WordRepository repository;

    @GetMapping("")
    public Iterable<WordEntity> getAllWords() {
        System.out.println("stuffs");
        return repository.findAll();
    }

    @PostMapping("")
    public WordEntity saveWord(@RequestBody @NotNull WordEntity word) {
        repository.save(word);
        return word;
    }

}
