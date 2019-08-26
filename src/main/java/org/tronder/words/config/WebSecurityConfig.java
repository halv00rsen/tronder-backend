package org.tronder.words.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.logout().invalidateHttpSession(true).deleteCookies("JSESSIONID").

        http
        .httpBasic()
        .and()
        .logout().clearAuthentication(true)
        .logoutSuccessUrl("/")
        .deleteCookies("JSESSIONID")
        .invalidateHttpSession(true);

        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/word").permitAll();
        http.authorizeRequests().anyRequest()
            .authenticated().and().httpBasic();

        http.csrf().disable();
        http.cors();
    }

}
