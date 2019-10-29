package org.tronder.words.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class SigningKeyResolverTest {

    @MockBean
    private JwtKeysConfig jwtKeysConfig;
    @MockBean
    private JwsHeader jwsHeader;

    private SigningKeyResolver signingKeyResolver;
    private Map<String, String> keyMap;
    private RSAKeyGenerator generator;

    @Before
    public void init() {
        generator = new RSAKeyGenerator();
        keyMap = new HashMap<>();
        keyMap.put("key", "keyId");
        String encodedModulus = encodeBigIntToString(generator.getPublicKey().getModulus());
        String encodedExponent = encodeBigIntToString(generator.getPublicKey().getPublicExponent());
        keyMap.put("modulus", encodedModulus);
        keyMap.put("exponent", encodedExponent);
        Mockito.when(jwtKeysConfig.getKeyModulusExponent()).thenReturn(Arrays.asList(keyMap));
    }

    @Test
    public void initialize() {
        signingKeyResolver = new SigningKeyResolver(jwtKeysConfig);
    }

    @Test
    public void testResolveKey() {
        initialize();
        Mockito.when(jwsHeader.getKeyId()).thenReturn(keyMap.get("key"));
        Key key = signingKeyResolver.resolveSigningKey(jwsHeader, (Claims) null);
        assertEquals(key, generator.getPublicKey());
        assertEquals(key.getAlgorithm(), generator.getPublicKey().getAlgorithm());
        assertTrue(key instanceof RSAPublicKey);
        RSAPublicKey publicKey = (RSAPublicKey) key;
        assertThat(publicKey.getPublicExponent(), is(generator.getPublicKey().getPublicExponent()));
        assertThat(publicKey.getModulus(), is(generator.getPublicKey().getModulus()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidKeyName() {
        initialize();
        Mockito.when(jwsHeader.getKeyId()).thenReturn("some-wrong-key");
        signingKeyResolver.resolveSigningKey(jwsHeader, (Claims) null);
    }


    private String encodeBigIntToString(BigInteger bigInteger) {
        return Base64.getUrlEncoder().encodeToString(bigInteger.toByteArray());
    }
}
