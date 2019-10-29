package org.tronder.words.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.tronder.words.errors.NotFoundError;
import org.tronder.words.model.Dialect;
import org.tronder.words.model.Hallmark;
import org.tronder.words.model.WordEntity;
import org.tronder.words.repository.DialectRepository;
import org.tronder.words.repository.WordRepository;

import java.util.*;


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

    @MockBean
    private HallmarkService hallmarkService;

    private DialectService dialectService;

    private WordEntity wordOnePublic;
    private WordEntity wordTwoPrivate;
    private Dialect publicDialect;
    private Dialect privateDialect;

    @Before
    public void setup() {
        Set<Hallmark> hallmarks = new HashSet<>();
        dialectService = new DialectService(dialectRepository, wordRepository, hallmarkService);
        wordOnePublic = new WordEntity(1, "This is word", "This is translation");
        wordTwoPrivate = new WordEntity(2, "Another word", "Another translation");
        publicDialect = new Dialect(
                1, "displayNameOne", wordsAsList(wordOnePublic), publicUser,
                true, "someDescription", hallmarks);
        privateDialect = new Dialect(
                2, "privateDialect", wordsAsList(wordTwoPrivate), privateUser,
                false, "anotherDescription", hallmarks);
        Mockito.when(dialectRepository.findById(privateDialect.getId())).thenReturn(Optional.of(privateDialect));
        Mockito.when(dialectRepository.findById(publicDialect.getId())).thenReturn(Optional.of(publicDialect));
    }

    private List<WordEntity> wordsAsList(WordEntity... words) {
        return new ArrayList<>(Arrays.asList(words));
    }

    @Test
    public void testGetAllPublicDialects() {
        Mockito.when(dialectRepository.findAllByPublicDialectIsTrue()).thenReturn(Collections.singletonList(publicDialect));
        List<Dialect> dialects = dialectService.getPublicDialects();
        assertThat(dialects.size(), is(1));
        assertTrue(dialects.contains(publicDialect));
        assertFalse(dialects.contains(privateDialect));
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

    @Test
    public void testAddDialect() {
        Mockito.when(dialectRepository.save(publicDialect)).thenReturn(publicDialect);
        Dialect dialect = dialectService.addDialect(publicDialect);
        assertEquals(dialect, publicDialect);
    }

    @Test
    public void testGetPublicDialects() {
        Mockito.when(dialectRepository.findAllByPublicDialectIsTrueOrCreatedByEquals(Mockito.anyString())).thenReturn(Collections.singletonList(publicDialect));
        List<Dialect> dialects = dialectService.getPublicAndUserDialects("some user");
        assertThat(dialects.size(), is(1));
        assertTrue(dialects.get(0).isPublicDialect());
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
