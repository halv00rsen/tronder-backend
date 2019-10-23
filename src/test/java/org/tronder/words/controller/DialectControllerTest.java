package org.tronder.words.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.tronder.words.dataAccessObject.DialectDTO;
import org.tronder.words.dataAccessObject.WordDTO;
import org.tronder.words.errors.BadRequestException;
import org.tronder.words.model.Dialect;
import org.tronder.words.model.Role;
import org.tronder.words.model.UserData;
import org.tronder.words.model.WordEntity;
import org.tronder.words.repository.RoleRepository;
import org.tronder.words.service.DialectService;
import org.tronder.words.utils.AuthenticationUtil;
import org.tronder.words.utils.JwtParser;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(DialectController.class)
public class DialectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DialectService dialectService;

    @MockBean
    private AuthenticationUtil authenticationUtil;

    @MockBean
    private JwtParser jwtParser;

    @MockBean
    private RoleRepository roleRepository;

    private Dialect dialect;
    private DialectDTO dialectDTO;
    private UserData userData;
    private Role role;
    private WordEntity word;
    private WordDTO wordDTO;


    @Before
    public void init() {
        dialectDTO = new DialectDTO("displayName", "some description", true);
        role = new Role();
        role.setAuthority("USER");
        dialect = new Dialect();
        dialect.setPublicDialect(dialectDTO.isPublicDialect());
        dialect.setDisplayName(dialectDTO.getDisplayName());
        dialect.setDescription(dialectDTO.getDescription());
        word = new WordEntity(1, "text of word", "transaltion of word");
        word.setDescription("some description");
        wordDTO = new WordDTO(word.getWordText(), word.getTranslation(), word.getDescription());
        userData = new UserData();
        userData.setSub("some-sub");
        userData.setName("name");
        userData.setEmail("email");
    }

    @Test
    public void testGetDialectsNoAuth() throws Exception {
        Mockito.when(dialectService.getPublicDialects()).thenReturn(Arrays.asList(dialect));
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
        Mockito.when(dialectService.getWordsInDialect(1, "")).thenReturn(Arrays.asList(word));

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


    @Test
    public void testGetWordsInDialectWithAuth() throws Exception {
    }

    @Test
    public void testAddWordToDialect() throws Exception {
        Mockito.when(dialectService.addWordToDialect(eq(1), Mockito.any(WordEntity.class), eq(userData.getSub()))).thenReturn(word);
        Mockito.when(authenticationUtil.getUserData()).thenReturn(userData);
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

    private MockHttpSession generateAuthSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                new MockSecurityContext(getMockAuthentication()));
        return session;
    }

    private Authentication getMockAuthentication() {
        return new UsernamePasswordAuthenticationToken(userData, null, Arrays.asList(role));
    }

    private void mockAuthentication(boolean authenticated) {
        Mockito.when(authenticationUtil.isAuthenticated()).thenReturn(authenticated);
    }

    public static String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class MockSecurityContext implements SecurityContext {

        private static final long serialVersionUID = -1386535243513362694L;

        private Authentication authentication;

        public MockSecurityContext(Authentication authentication) {
            this.authentication = authentication;
        }

        @Override
        public Authentication getAuthentication() {
            return this.authentication;
        }

        @Override
        public void setAuthentication(Authentication authentication) {
            this.authentication = authentication;
        }
    }
}
