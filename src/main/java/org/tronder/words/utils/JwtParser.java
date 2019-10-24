package org.tronder.words.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tronder.words.model.UserData;

@Component
public class JwtParser {

    private final SigningKeyResolverAdapter signingKeyResolver;

    @Autowired
    public JwtParser(SigningKeyResolverAdapter signingKeyResolver) {
        this.signingKeyResolver = signingKeyResolver;
    }

    public UserData parseTokenToData(String token) {
        Claims claims = getGeneratedClaimsFromToken(token);
        if (!isIdToken(claims)) {
            throw new IllegalArgumentException("Expected ID token, got something else");
        }
        UserData userData = new UserData();
        userData.setSub(getSubFromToken(claims));
        userData.setName(getNameFromToken(claims));
        userData.setEmail(getEmailFromToken(claims));
        return userData;
    }

    private boolean isIdToken(Claims claims) {
        return "id".equals(claims.get("token_use", String.class));
    }

    private String getEmailFromToken(Claims claims) {
        return claims.get("email", String.class);
    }

    private String getNameFromToken(Claims claims) {
        return claims.get("name", String.class);
    }

    private String getSubFromToken(Claims claims) {
        return claims.getSubject();
    }

    private Claims getGeneratedClaimsFromToken(String token) {
        return Jwts.parser().setSigningKeyResolver(signingKeyResolver).parseClaimsJws(token).getBody();
    }
}
