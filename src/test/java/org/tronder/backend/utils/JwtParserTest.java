package org.tronder.backend.utils;

import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
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
