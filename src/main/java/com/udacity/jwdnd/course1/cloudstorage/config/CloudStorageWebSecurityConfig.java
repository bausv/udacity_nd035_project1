package com.udacity.jwdnd.course1.cloudstorage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class CloudStorageWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CloudStorageAuthenticationService authenticationService;

    @Autowired
    public CloudStorageWebSecurityConfig(CloudStorageAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(this.authenticationService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/signup", "/css/**", "/js/**").permitAll().anyRequest().authenticated();

        http.formLogin().loginPage("/login").permitAll();
        http.formLogin().defaultSuccessUrl("/home", true);
    }
}
