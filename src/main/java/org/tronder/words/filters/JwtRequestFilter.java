package org.tronder.words.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tronder.words.model.UserData;
import org.tronder.words.repository.RoleRepository;
import org.tronder.words.utils.JwtParser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtParser jwtParser;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            try {
                UserData userData = jwtParser.parseTokenToData(jwtToken);
                Authentication auth = new UsernamePasswordAuthenticationToken(userData, null, roleRepository.findAll());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (IllegalArgumentException e) {
                System.out.println("Illegal token");
                e.printStackTrace();
            }
        }
        filterChain.doFilter(request, response);
    }
}
