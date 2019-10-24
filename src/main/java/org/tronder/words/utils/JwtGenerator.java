package org.tronder.words.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.*;
import java.util.Date;
import java.util.Map;

@Component
public class JwtGenerator extends SigningKeyResolverAdapter implements Serializable {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtGenerator() {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }


    public String generateJwtToken(Map<String, Object> claims) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId("someId")
                .setIssuer("tronder-issuer")
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate(60000))
                .signWith(signatureAlgorithm, privateKey);
        return builder.compact();
    }

    private Date getExpirationDate(long timeInMilli) {
        return new Date(System.currentTimeMillis() + timeInMilli);
    }


    public Claims getClaimsFromToken(String token) {
        Jws<Claims> jwt = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
        return jwt.getBody();
    }

    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
        return publicKey;
    }
}
