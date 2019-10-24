package org.tronder.words.utils;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RSAKeyGeneratorTest {

    private RSAKeyGenerator generator;

    @Before
    public void init() {
        generator = new RSAKeyGenerator();
    }

    @Test
    public void testGettingKeys() {
        assertThat(generator.getPublicKey(), instanceOf(RSAPublicKey.class));
        assertThat(generator.getPrivateKey(), instanceOf(RSAPrivateKey.class));
    }

    @Test
    public void testUniqueKeysDifferentObjects() {
        RSAKeyGenerator generator = new RSAKeyGenerator();
        assertThat(generator.getPrivateKey(), is(not(this.generator.getPrivateKey())));
        assertThat(generator.getPublicKey(), is(not(this.generator.getPublicKey())));
    }
}
