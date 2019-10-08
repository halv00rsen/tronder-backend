package org.tronder.words.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tronder.words.model.Role;
import org.tronder.words.model.User;
import org.tronder.words.repository.RoleRepository;
import org.tronder.words.service.UserService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .httpBasic()
            .and()
            .logout().clearAuthentication(true)
            .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true);

        http.authorizeRequests()
            .antMatchers("/user/**").hasAuthority("ADMIN");
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/word").permitAll();
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/username").permitAll();
        http.authorizeRequests().anyRequest()
            .authenticated().and().httpBasic();

        http.csrf().disable();
        http.cors();
    }


    @Autowired
    private UserService userService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
          = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        System.out.println("javel?");
        return authProvider;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
    throws Exception {
        // auth.jdbcAuthentication()
        //     .dataSource(dataSource);
        Role role = new Role();
        role.setAuthority("ADMIN");
        roleRepository.save(role);

        User user = new User();
        user.setEnabled(true);
        user.setPassword(passwordEncoder().encode("passord"));
        user.setUsername("jorgen");
        user.setRole(role);
        userService.createUser(user);

        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
