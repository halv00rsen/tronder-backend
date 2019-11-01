package org.tronder.words.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tronder.words.errors.NotFoundError;
import org.tronder.words.errors.UserNotFoundException;
import org.tronder.words.model.Dialect;
import org.tronder.words.model.WordEntity;
import org.tronder.words.repository.DialectRepository;
import org.tronder.words.repository.WordRepository;

@Service
public class DialectService {

    private final DialectRepository dialectRepository;
    private final WordRepository wordRepository;
    private final HallmarkService hallmarkService;
    private final UserService userService;

    @Autowired
    public DialectService(
            DialectRepository dialectRepository,
            WordRepository wordRepository,
            HallmarkService hallmarkService,
            UserService userService
    ) {
        this.dialectRepository = dialectRepository;
        this.wordRepository = wordRepository;
        this.hallmarkService = hallmarkService;
        this.userService = userService;
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
        List<Dialect> dialects = dialectRepository.findAllByPublicDialectIsTrue();
        setUserInfoDialects(dialects);
        return dialects;
    }


    public List<Dialect> getPublicAndUserDialects(String userSub) {
        List<Dialect> dialects = dialectRepository.findAllByPublicDialectIsTrueOrCreatedByEquals(userSub);
        setUserInfoDialects(dialects);
        return dialects;
    }


    private void setUserInfoDialects(List<Dialect> dialects) {
        for (Dialect dialect : dialects) {
            try {
                dialect.setUserProviderDTO(userService.getUserInfo(dialect.getCreatedBy()));
            } catch (UserNotFoundException e) {
            }
        }
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
