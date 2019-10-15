package org.tronder.words.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tronder.words.errors.NotFoundError;
import org.tronder.words.model.Dialect;
import org.tronder.words.model.WordEntity;
import org.tronder.words.repository.DialectRepository;
import org.tronder.words.repository.WordRepository;

@Service
public class DialectService {

    private final DialectRepository dialectRepository;
    private final WordRepository wordRepository;

    @Autowired
    public DialectService(DialectRepository dialectRepository, WordRepository wordRepository) {
        this.dialectRepository = dialectRepository;
        this.wordRepository = wordRepository;
    }

    public Dialect addDialect(Dialect dialect) {
        return dialectRepository.save(dialect);
    }

    public WordEntity addWordToDialect(int dialectId, WordEntity word) {
        Dialect dialect = getDialectById(dialectId);
        wordRepository.save(word);
        dialect.addWord(word);
        dialectRepository.save(dialect);
        return word;
    }

    public List<WordEntity> getWordsInDialect(int dialectId) {
        return getDialectById(dialectId).getWords();
    }

    public Iterable<Dialect> getDialects() {
        return dialectRepository.findAll();
    }

    private Dialect getDialectById(int dialectId) {
        Optional<Dialect> hasDialect = dialectRepository.findById(dialectId);
        if (hasDialect.isPresent()) {
            return hasDialect.get();
        }
        throw new NotFoundError();
    }

}
