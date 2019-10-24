package org.tronder.words.utils;

import io.jsonwebtoken.MalformedJwtException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tronder.words.model.UserData;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

public class JwtParserTest {

    private JwtGenerator generator;
    private JwtParser jwtParser;
    private Map<String, Object> claims;

    @Before
    public void init() {
        generator = new JwtGenerator();
        jwtParser = new JwtParser(generator);
        claims = new HashMap<>();
        claims.put("name", "Some Name");
        claims.put("email", "this@email.com");
        claims.put("token_use", "id");
        claims.put("sub", "thisIsSub");
    }

    @Test
    public void testParseValidToken() {
        UserData userData = jwtParser.parseTokenToData(generateToken());
        Assert.assertThat(userData.getSub(), is(claims.get("sub")));
        Assert.assertThat(userData.getEmail(), is(claims.get("email")));
        Assert.assertThat(userData.getName(), is(claims.get("name")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotIdToken() {
        claims.put("token_use", "notId");
        jwtParser.parseTokenToData(generateToken());
    }

    @Test(expected = MalformedJwtException.class)
    public void testInvalidJwtToken() {
        jwtParser.parseTokenToData("invalidtoken");
    }

    @Test(expected = MalformedJwtException.class)
    public void testInvalidJwtTokenWithPeriods() {
        jwtParser.parseTokenToData("header.body.signing");
    }

    private String generateToken() {
        return generator.generateJwtToken(claims);
    }
}
