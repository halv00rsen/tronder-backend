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
    private final HallmarkService hallmarkService;

    @Autowired
    public DialectService(
            DialectRepository dialectRepository,
            WordRepository wordRepository,
            HallmarkService hallmarkService) {
        this.dialectRepository = dialectRepository;
        this.wordRepository = wordRepository;
        this.hallmarkService = hallmarkService;
    }

    public Dialect addDialect(Dialect dialect) {
        hallmarkService.saveHallmarks(dialect.getHallmarksSet());
        return dialectRepository.save(dialect);
    }

    public WordEntity addWordToDialect(int dialectId, WordEntity word, String userSub) {
        Dialect dialect = getPublicOrUserDialectById(dialectId, userSub);
        wordRepository.save(word);
        dialect.addWord(word);
        dialectRepository.save(dialect);
        return word;
    }

    public List<WordEntity> getWordsInDialectNoAuth(int dialectId) {
        return getWordsInDialect(dialectId, "");
    }

    public List<WordEntity> getWordsInDialect(int dialectId, String userSub) {
        return getPublicOrUserDialectById(dialectId, userSub).getWords();
    }

    public List<Dialect> getPublicDialects() {
        return dialectRepository.findAllByPublicDialectIsTrue();
    }

    public List<Dialect> getPublicAndUserDialects(String userSub) {
        return dialectRepository.findAllByPublicDialectIsTrueOrCreatedByEquals(userSub);
    }

    private Dialect getPublicOrUserDialectById(int dialectId, String userSub) {
        Optional<Dialect> hasDialect = dialectRepository.findById(dialectId);
        if (hasDialect.isPresent()) {
            Dialect dialect = hasDialect.get();
            if (dialect.isPublicDialect() || dialect.getCreatedBy().equals(userSub)) {
               return dialect;
            }
        }
        throw new NotFoundError();
    }

}
