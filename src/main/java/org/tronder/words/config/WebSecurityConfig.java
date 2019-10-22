package org.tronder.words.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.tronder.words.filters.JwtRequestFilter;
import org.tronder.words.model.Role;
import org.tronder.words.repository.RoleRepository;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
            .antMatchers("/user/**").hasAuthority("ADMIN");

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/dialect/**").permitAll();
        http.authorizeRequests().antMatchers("/dialect/**")
                .hasAuthority("ROLE_USER");
        http.authorizeRequests().anyRequest()
            .authenticated();

        http.csrf().disable();
        http.cors();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        Role role = new Role();
        role.setAuthority("ADMIN");
        roleRepository.save(role);
        Role role2 = new Role();
        role2.setAuthority("USER");
        roleRepository.save(role2);
    }
}
