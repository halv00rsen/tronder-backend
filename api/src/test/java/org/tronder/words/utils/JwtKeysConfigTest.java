package org.tronder.words.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.junit.Assert.*;

public class JwtKeysConfigTest {

    private JwtKeysConfig jwtKeysConfig;

    @Before
    public void init() {
        jwtKeysConfig = new JwtKeysConfig();
        setField(jwtKeysConfig, "codedModulusOne", "modulusOne");
        setField(jwtKeysConfig, "codedModulusTwo", "modulusTwo");
        setField(jwtKeysConfig, "kidOne", "kidOne");
        setField(jwtKeysConfig, "kidTwo", "kidTwo");
        setField(jwtKeysConfig, "exponentOne", "exponentOne");
        setField(jwtKeysConfig, "exponentTwo", "exponentTwo");
    }

    @Test
    public void testGetKeys() {
        List<Map<String, String>> keys = jwtKeysConfig.getKeyModulusExponent();
        assertThat(keys.size(), is(2));
        Map<String, String> first = keys.get(0);
        assertEquals(first.get("key"), "kidOne");
        assertEquals(first.get("modulus"), "modulusOne");
        assertEquals(first.get("exponent"), "exponentOne");
        Map<String, String> second = keys.get(1);
        assertEquals(second.get("key"), "kidTwo");
        assertEquals(second.get("modulus"), "modulusTwo");
        assertEquals(second.get("exponent"), "exponentTwo");
    }
}
