package org.tronder.words.filters;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tronder.words.errors.NotAuthenticatedException;
import org.tronder.words.model.UserData;
import org.tronder.words.repository.RoleRepository;
import org.tronder.words.utils.AuthenticationUtil;
import org.tronder.words.utils.JwtParser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtRequestFilter extends OncePerRequestFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtParser jwtParser;

    @Autowired
    private AuthenticationUtil authenticationUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        Authentication auth = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            try {
                UserData userData = jwtParser.parseTokenToData(jwtToken);
                auth = new UsernamePasswordAuthenticationToken(userData, null, roleRepository.findAll());
            } catch (IllegalArgumentException e) {
                throw new NotAuthenticatedException("Something is wrong with the JWT token. Is it an id token?");
            } catch (ExpiredJwtException ejw) {
                throw new NotAuthenticatedException("The token has expired.");
            }
        }
        authenticationUtil.setCurrentAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}
