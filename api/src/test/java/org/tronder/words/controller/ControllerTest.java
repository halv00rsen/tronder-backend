package org.tronder.words.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.tronder.words.model.Role;
import org.tronder.words.model.UserData;
import org.tronder.words.repository.RoleRepository;
import org.tronder.words.utils.AuthenticationUtil;
import org.tronder.words.utils.JwtParser;

import java.util.Collections;

@RunWith(SpringRunner.class)
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected JwtParser jwtParser;

    protected UserData userData;

    @MockBean
    private AuthenticationUtil authenticationUtil;

    @MockBean
    private RoleRepository roleRepository;

    private Role role;


    @Before
    public final void abstractInit() {
        role = new Role();
        role.setAuthority("USER");
        userData = new UserData();
        userData.setSub("some-sub");
        userData.setName("name");
        userData.setEmail("email");
        Mockito.when(roleRepository.findAll()).thenReturn(Collections.singletonList(role));
    }


    public final String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    AuthenticationUtil getAuthenticationUtil() {
        return authenticationUtil;
    }

    MockHttpSession generateAuthSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                new MockSecurityContext(getMockAuthentication()));
        return session;
    }

    private Authentication getMockAuthentication() {
        return new UsernamePasswordAuthenticationToken(userData, null, Collections.singletonList(role));
    }

    void mockAuthentication(boolean authenticated) {
        Mockito.when(authenticationUtil.isAuthenticated()).thenReturn(authenticated);
    }


    public static class MockSecurityContext implements SecurityContext {

        private static final long serialVersionUID = -1386535243513362694L;

        private Authentication authentication;

        MockSecurityContext(Authentication authentication) {
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
    }}
