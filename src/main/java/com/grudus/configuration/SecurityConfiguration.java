package com.grudus.configuration;

import com.grudus.configuration.authentication.*;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    private final TokenAuthenticationService tokenAuthenticationService;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UserDetailsServiceImpl userDetailsService;


    @Autowired
    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, TokenAuthenticationService tokenAuthenticationService, UserAuthenticationProvider userAuthenticationProvider, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/api/user/add", "/api/user/add/**").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("api/user/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
                .and()
        .addFilterBefore(new StatelessLoginFilter("/login", tokenAuthenticationService, userAuthenticationProvider),
                UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(userAuthenticationProvider)
                .userDetailsService(userDetailsService);
    }

}
