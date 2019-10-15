package org.tronder.backend.utils;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.tronder.words.utils.JwtKeysConfig;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.*;

public class JwtKeysConfigTest {

    private JwtKeysConfig jwtKeysConfig;

    @Before
    public void init() {
        jwtKeysConfig = new JwtKeysConfig();
//        ReflectionTestUtils.setField(jwtKeysConfig, "codedModulusOne", "modulusOne");
//        ReflectionTestUtils.setField(jwtKeysConfig, "codedModulusTwo", "modulusTwo");
//        ReflectionTestUtils.setField(jwtKeysConfig, "kidOne", "keyOne");
//        ReflectionTestUtils.setField(jwtKeysConfig, "kidTwo", "keyTwo");
    }

    @Test
    public void testValidKeysAndModules() {
        List<Map<String, String>> keys = jwtKeysConfig.getKeysAndModulus();
        System.out.println(keys);
        assertThat(keys.size(), is(2));
    }

}
