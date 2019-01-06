package com.tablefootbal.server.security.bruteforce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements
        ApplicationListener<AuthenticationSuccessEvent> {

    final private
    LoginTrackerService loginTrackerService;

    @Autowired
    public AuthenticationSuccessListener(LoginTrackerService loginTrackerService) {
        this.loginTrackerService = loginTrackerService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        loginTrackerService.handleSuccessFromIp(details.getRemoteAddress());
    }
}


