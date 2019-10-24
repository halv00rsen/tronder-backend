package org.tronder.words.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class SigningKeyResolver extends SigningKeyResolverAdapter implements Serializable {

    private final Map<String, PublicKey> keys = new HashMap<>();

    @Autowired
    public SigningKeyResolver(JwtKeysConfig keysConfig) {
        keysConfig.getKeyModulusExponent().forEach(keyMap -> {
            keys.put(keyMap.get("key"), generatePublicKey(keyMap.get("modulus"), keyMap.get("exponent")));
        });
    }

    private PublicKey generatePublicKey(String codedModulus, String codedExponent) {
        byte[] decodedModulus = Base64.getUrlDecoder().decode(codedModulus);
        byte[] decodedExponent = Base64.getUrlDecoder().decode(codedExponent);

        BigInteger modulus = new BigInteger(1, decodedModulus);
        BigInteger exponent = new BigInteger(1, decodedExponent);

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory keyFactory;

        try {
            keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not process the jwt token");
        }
    }

    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
        for (Map.Entry<String, PublicKey> value : keys.entrySet()) {
            if (value.getKey().equals(jwsHeader.getKeyId())) {
                return value.getValue();
            }
        }
        throw new IllegalArgumentException("No valid key found in JWT header");
    }


}
