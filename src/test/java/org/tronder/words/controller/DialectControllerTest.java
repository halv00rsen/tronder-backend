package org.tronder.words.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.tronder.words.dataAccessObject.DialectDTO;
import org.tronder.words.dataAccessObject.WordDTO;
import org.tronder.words.model.Dialect;
import org.tronder.words.model.WordEntity;
import org.tronder.words.service.DialectService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.Assert.*;

@WebMvcTest(DialectController.class)
public class DialectControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DialectService dialectService;

    private Dialect dialect;
    private DialectDTO dialectDTO;
    private WordEntity word;
    private WordDTO wordDTO;


    @Before
    public void init() {
        List<String> hallmarks = new ArrayList<>();
        dialectDTO = new DialectDTO("displayName", "some description", true, hallmarks);
        dialect = new Dialect();
        dialect.setPublicDialect(dialectDTO.isPublicDialect());
        dialect.setDisplayName(dialectDTO.getDisplayName());
        dialect.setDescription(dialectDTO.getDescription());
        word = new WordEntity(1, "text of word", "transaltion of word");
        word.setDescription("some description");
        wordDTO = new WordDTO(word.getWordText(), word.getTranslation(), word.getDescription());
    }

    @Test
    public void testGetDialectsNoAuth() throws Exception {
        Mockito.when(dialectService.getPublicDialects()).thenReturn(Collections.singletonList(dialect));
        mockAuthentication(false);
        MvcResult result = mockMvc.perform(get("/dialect"))
                .andExpect(status().isOk()).andReturn();
        String resultString = result.getResponse().getContentAsString();
        List<Object> dialects = new ObjectMapper()
                .readValue(resultString, new TypeReference<List<Object>>(){});
        assertThat(dialects.size(), is(1));
    }

    @Test()
    public void createNewDialectNoAuth() throws Exception {
        mockMvc.perform(post("/dialect")).andExpect(status().isForbidden());
    }

    @Test
    public void createNewDialect() throws Exception {
        Mockito.when(dialectService.addDialect(Mockito.any(Dialect.class))).thenReturn(dialect);
        Mockito.when(jwtParser.parseTokenToData("someKey")).thenReturn(userData);

        mockMvc.perform(
                post("/dialect").session(generateAuthSession())
                        .header("Authorization", "Bearer someKey")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(dialectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numWords", is(0)))
                .andExpect(jsonPath("$.description", is(dialectDTO.getDescription())))
                .andExpect(jsonPath("$.publicDialect", is(dialectDTO.isPublicDialect())))
                .andExpect(jsonPath("$.displayName", is(dialectDTO.getDisplayName())));
    }

    @Test
    public void testGetWordsInDialectNoAuth() throws Exception {
        Mockito.when(dialectService.getWordsInDialect(1, "")).thenReturn(Collections.singletonList(word));

        mockMvc.perform(
                get("/dialect/1/word")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].wordText", is(word.getWordText())))
                .andExpect(jsonPath("$[0].description", is(word.getDescription())))
                .andExpect(jsonPath("$[0].translation", is(word.getTranslation())));
    }

    @Test
    public void testSetEmptyDialectName() throws Exception {
        dialectDTO.setDisplayName("   ");
        mockMvc.perform(
                post("/dialect").session(generateAuthSession())
                .content(asJsonString(dialectDTO)).contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(status().isBadRequest());
    }


//    @Test
//    public void testGetWordsInDialectWithAuth() throws Exception {
//    }

    @Test
    public void testAddWordToDialect() throws Exception {
        Mockito.when(dialectService.addWordToDialect(eq(1), Mockito.any(WordEntity.class), eq(userData.getSub()))).thenReturn(word);
        Mockito.when(getAuthenticationUtil().getUserData()).thenReturn(userData);
        mockMvc.perform(
                post("/dialect/1/word")
                        .session(generateAuthSession())
                        .content(asJsonString(wordDTO))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wordText", is(word.getWordText())))
                .andExpect(jsonPath("$.description", is(word.getDescription())))
                .andExpect(jsonPath("$.translation", is(word.getTranslation())));
    }

    @Test
    public void testAddInvalidWord() throws Exception {
        mockMvc.perform(post("/dialect/1/word").session(generateAuthSession()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddWordInvalidUrl() throws Exception {
        mockMvc.perform(
                post("/dialect/someString/word")
                        .session(generateAuthSession())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(wordDTO))
        )
                .andExpect(status().isBadRequest());
    }




}
