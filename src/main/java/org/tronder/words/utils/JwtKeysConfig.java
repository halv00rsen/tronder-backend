package org.tronder.words.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class JwtKeysConfig {


    @Value("${jwt.keys.one.modulus}")
    private String codedModulusOne;

    @Value("${jwt.keys.two.modulus}")
    private String codedModulusTwo;

    @Value("${jwt.keys.one.kid}")
    private String kidOne;

    @Value("${jwt.keys.two.kid}")
    private String kidTwo;

    @Value("${jwt.keys.one.exponent}")
    private String exponentOne;

    @Value("${jwt.keys.two.exponent}")
    private String exponentTwo;


    public List<Map<String, String>> getKeyModulusExponent() {
        Map<String, String> firstMap = new HashMap<>();
        firstMap.put("key", kidOne);
        firstMap.put("modulus", codedModulusOne);
        firstMap.put("exponent", exponentOne);
        Map<String, String> secondMap = new HashMap<>();
        secondMap.put("key", kidTwo);
        secondMap.put("modulus", codedModulusTwo);
        secondMap.put("exponent", exponentTwo);
        return Arrays.asList(firstMap, secondMap);
    }

}
