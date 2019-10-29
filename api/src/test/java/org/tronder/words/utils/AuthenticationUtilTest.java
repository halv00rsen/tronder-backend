package org.tronder.words.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.tronder.words.errors.NotAuthenticatedException;
import org.tronder.words.model.UserData;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class AuthenticationUtilTest {

    @MockBean
    private Authentication authentication;

    @MockBean
    private SecurityContext securityContext;

    private UserData userData;

    private AuthenticationUtil authenticationUtil;

    @Before
    public void init() {
        authenticationUtil = new AuthenticationUtil();
        userData = new UserData();
        userData.setSub("sub");
        userData.setEmail("email");
        userData.setName("name");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetUserData() {
        Mockito.when(authentication.getPrincipal()).thenReturn(userData);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        UserData userData = authenticationUtil.getUserData();
        Assert.assertThat(userData.getName(), is(this.userData.getName()));
        Assert.assertThat(userData.getSub(), is(this.userData.getSub()));
        Assert.assertThat(userData.getEmail(), is(this.userData.getEmail()));
    }

    @Test
    public void testIsAuthenticated() {
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getPrincipal()).thenReturn(userData);
        assertTrue(authenticationUtil.isAuthenticated());
    }

    @Test
    public void testNotAuthenticated() {
        Mockito.when(authentication.isAuthenticated()).thenReturn(false);
        assertFalse(authenticationUtil.isAuthenticated());
    }

    @Test
    public void testNotAuthenticatedAnonymousUser() {
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getPrincipal()).thenReturn("anonymousUser");
        assertFalse(authenticationUtil.isAuthenticated());
    }

    @Test(expected = NotAuthenticatedException.class)
    public void testGetUserDataNotAuthenticated() {
        Mockito.when(authentication.isAuthenticated()).thenReturn(false);
        authenticationUtil.getUserData();
    }

    @Test
    public void testSetAuthentication() {
        authenticationUtil.setCurrentAuthentication(authentication);
        assertEquals(authenticationUtil.getCurrentAuthentication(), authentication);
    }
}
