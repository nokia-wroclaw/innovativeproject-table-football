package com.tablefootbal.server.config;

import com.tablefootbal.server.security.bruteforce.*;
import com.tablefootbal.server.security.bruteforce.filter.CustomBasicAuthenticationFilter;
import com.tablefootbal.server.security.bruteforce.filter.IpBlockFilter;
import com.tablefootbal.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.GenericFilter;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final LoginTrackerService loginTrackerService;
    private final MessageSource messageSource;
    private final UserService userService;

    @Autowired
    public SecurityConfiguration(LoginTrackerService loginTrackerService, MessageSource messageSource, UserService userService) {
        this.loginTrackerService = loginTrackerService;
        this.messageSource = messageSource;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/sensor/**").hasAnyRole("SENSOR", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/*").permitAll()
                .and()
                .requiresChannel().antMatchers("/sensor/**").requiresSecure()
                .and()
                .requiresChannel().antMatchers("/admin/**").requiresSecure()
                .and()
                .cors()
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .and()
                .addFilterAt(basicAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(ipBlockFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    GenericFilter ipBlockFilter() {
        return new IpBlockFilter(loginTrackerService, messageSource);
    }

    @Bean
    CustomBasicAuthenticationFilter basicAuthenticationFilter() throws Exception {
        return new CustomBasicAuthenticationFilter(
                authenticationManager(),
                authenticationEntryPoint(),
                loginTrackerService);
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
