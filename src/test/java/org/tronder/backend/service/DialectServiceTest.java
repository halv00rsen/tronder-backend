package org.tronder.backend.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.tronder.words.errors.NotFoundError;
import org.tronder.words.model.Dialect;
import org.tronder.words.model.WordEntity;
import org.tronder.words.repository.DialectRepository;
import org.tronder.words.repository.WordRepository;
import org.tronder.words.service.DialectService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class DialectServiceTest {

    private final String privateUser = "userOne";
    private final String publicUser = "userTwo";

    @MockBean
    private DialectRepository dialectRepository;

    @MockBean
    private WordRepository wordRepository;

    private DialectService dialectService;

    private WordEntity wordOnePublic;
    private WordEntity wordTwoPrivate;
    private Dialect publicDialect;
    private Dialect privateDialect;

    @Before
    public void setup() {
        dialectService = new DialectService(dialectRepository, wordRepository);
        wordOnePublic = new WordEntity(1, "This is word", "This is translation");
        wordTwoPrivate = new WordEntity(2, "Another word", "Another translation");
        publicDialect = new Dialect(
                1, "displayNameOne", wordsAsList(wordOnePublic), publicUser,
                true, "someDescription");
        privateDialect = new Dialect(
                2, "privateDialect", wordsAsList(wordTwoPrivate), privateUser,
                false, "anotherDescription");
        Mockito.when(dialectRepository.findById(privateDialect.getId())).thenReturn(Optional.of(privateDialect));
        Mockito.when(dialectRepository.findById(publicDialect.getId())).thenReturn(Optional.of(publicDialect));
    }

    private List<WordEntity> wordsAsList(WordEntity... words) {
        return new ArrayList<WordEntity>(Arrays.asList(words));
    }

    @Test
    public void testGetAllPublicDialects() {
        Mockito.when(dialectRepository.findAllByPublicDialectIsTrue()).thenReturn(Arrays.asList(publicDialect));
        List<Dialect> dialects = dialectService.getPublicDialects();
        assertThat(dialects.size(), is(1));
        assertTrue(dialects.contains(publicDialect));
        assertTrue(!dialects.contains(privateDialect));
    }

    @Test(expected = NotFoundError.class)
    public void testPostWordToPrivateDialectError() {
        dialectService.addWordToDialect(privateDialect.getId(), wordOnePublic, publicUser);
    }

    @Test
    public void testAddWordToPublicNoOwner() {
        checkAddWord(privateUser);
    }

    @Test
    public void testAddWordToPublicOwner() {
        checkAddWord(publicUser);
    }

    private void checkAddWord(String userSub) {
        WordEntity word = dialectService.addWordToDialect(publicDialect.getId(), wordTwoPrivate, userSub);
        assertThat(word.getId(), is(wordTwoPrivate.getId()));
        assertThat(publicDialect.getWords().size(), is(2));
    }

    @Test
    public void testGetWordsInPublicDialect() {
        List<WordEntity> words = dialectService.getWordsInDialectNoAuth(publicDialect.getId());
        assertThat(words.size(), is(1));
        assertTrue(words.contains(wordOnePublic));
    }

    @Test(expected = NotFoundError.class)
    public void testGetWordsInPrivateDialectNotAuthenticated() {
        dialectService.getWordsInDialectNoAuth(privateDialect.getId());
    }

    @Test(expected = NotFoundError.class)
    public void testGetWordsInPrivateDialectNoAccess() {
        dialectService.getWordsInDialect(privateDialect.getId(), publicUser);
    }
}
