package com.tablefootbal.server.config;

import com.tablefootbal.server.entity.User;
import com.tablefootbal.server.entity.UserRole;
import com.tablefootbal.server.repository.UserRepository;
import com.tablefootbal.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.user.name}")
    private String sensorName;

    @Value("${spring.security.user.password}")
    private String sensorPassword;

    @Value("${admin.user.name")
    private String adminUsername;

    @Value("${admin.password")
    private String adminPassword;


    private final UserService userService;

    private final UserRepository userRepository;

    @Autowired
    public SecurityConfiguration(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/sensor/**").hasRole("SENSOR")
                .antMatchers("/*").permitAll()
                .and()
                .requiresChannel().antMatchers("/sensor/**").requiresSecure()
                .and()
                .httpBasic()
                .and()
                .authorizeRequests();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
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

    @EventListener(ApplicationStartedEvent.class)
    public void InitializeUsersInDb() {
        User user = new User();
        user.setUsername(sensorName);
        user.setPassword(passwordEncoder().encode(sensorPassword));

        UserRole role = new UserRole();
        role.setName(UserRole.USER_ROLES.ROLE_SENSOR);

        ArrayList<UserRole> roles = new ArrayList<>();
        roles.add(role);

        user.setRoles(roles);
        userRepository.save(user);

        user = new User();
        user.setUsername(adminUsername);
        user.setPassword(passwordEncoder().encode(adminPassword));

        role = new UserRole();
        role.setName(UserRole.USER_ROLES.ROLE_ADMIN);

        roles = new ArrayList<>();
        roles.add(role);

        user.setRoles(roles);
        userRepository.save(user);
    }
}
