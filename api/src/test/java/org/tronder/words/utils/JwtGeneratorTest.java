package org.tronder.words.utils;

import io.jsonwebtoken.Claims;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtGeneratorTest {

    private JwtGenerator generator;
    private Map<String, Object> claims;

    @Before
    public void init() {
        generator = new JwtGenerator();
        claims = new HashMap<>();
        claims.put("token_use", "id");
        claims.put("some_key", "some_value");
    }

    @Test
    public void testGenerationOfToken() {
        String token = generator.generateJwtToken(claims);
        Assert.assertEquals(2, token.chars().filter(ch -> ch == '.').count());
    }

    @Test
    public void testGettingClaimsFromToken() {
        String token = generator.generateJwtToken(claims);
        Claims claims = generator.getClaimsFromToken(token);
        Assert.assertThat(claims.get("token_use", String.class), is("id"));
        Assert.assertThat(claims.get("some_key", String.class), is("some_value"));
        Assert.assertThat(claims.get("iss", String.class), is("tronder-issuer"));
        Assert.assertThat(claims.get("jti", String.class), is("someId"));
        Assert.assertNotNull(claims.get("exp", Date.class));
        Assert.assertNotNull(claims.get("iat", Date.class));
    }
}
