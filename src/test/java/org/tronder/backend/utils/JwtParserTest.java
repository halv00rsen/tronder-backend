package org.tronder.backend.utils;

import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.tronder.words.utils.JwtParser;
import org.tronder.words.utils.SigningKeyResolver;

public class JwtParserTest {

    @Mock
    private SigningKeyResolver signingKeyResolver;

    private JwtParser jwtParser;

    @Before
    public void init() {
        jwtParser = new JwtParser(signingKeyResolver);
    }

    @Test
    public void parseTokenWithInvalidToken() {
        Mockito.when(
            signingKeyResolver.resolveSigningKey(Mockito.any(), Mockito.any(Claims.class))
        ).thenReturn(Mockito.any());
        System.out.println(jwtParser);
    }
    @Test
    public void digg() {
        System.out.println(jwtParser);
    }
}
