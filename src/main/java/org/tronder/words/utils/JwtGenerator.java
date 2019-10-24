package org.tronder.words.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtGenerator extends SigningKeyResolverAdapter implements Serializable {

    private final RSAKeyGenerator generator;

    public JwtGenerator() {
        generator = new RSAKeyGenerator();
    }


    public String generateJwtToken(Map<String, Object> claims) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId("someId")
                .setIssuer("tronder-issuer")
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate(60000))
                .signWith(signatureAlgorithm, generator.getPrivateKey());
        return builder.compact();
    }


    private Date getExpirationDate(long timeInMilli) {
        return new Date(System.currentTimeMillis() + timeInMilli);
    }


    public Claims getClaimsFromToken(String token) {
        Jws<Claims> jwt = Jwts.parser().setSigningKey(generator.getPublicKey()).parseClaimsJws(token);
        return jwt.getBody();
    }

    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
        return generator.getPublicKey();
    }
}
