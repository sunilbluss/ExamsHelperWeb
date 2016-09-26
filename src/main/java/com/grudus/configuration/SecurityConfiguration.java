package com.grudus.configuration;

import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final UserRepository userRepository;



    @Autowired
    public SecurityConfiguration(DataSource dataSource, TokenAuthenticationService tokenAuthenticationService, UserRepository userRepository) {
        this.dataSource = dataSource;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http .csrf().disable()
        .exceptionHandling()
                .and()
                .anonymous()
                .and()
                .servletApi()
                .and()
                .headers().cacheControl()
                .and()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/login").permitAll()
                .anyRequest().authenticated()
                .and()
        .addFilterBefore(new StatelessLoginFilter("/login", tokenAuthenticationService, userRepository),
                UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService),
                UsernamePasswordAuthenticationFilter.class)
        ;

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder());

        // TODO: 12.09.16 debug only --------------
        auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password("admin")
                .roles("ADMIN");
        // -----------------------------------
    }


    // TODO: 16.09.16 Disabled for api
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }
}
