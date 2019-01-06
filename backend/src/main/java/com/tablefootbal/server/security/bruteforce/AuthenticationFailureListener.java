package com.tablefootbal.server.security.bruteforce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener
        implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    final private
    LoginTrackerService authenticationTracker;

    @Autowired
    public AuthenticationFailureListener(LoginTrackerService authenticationTracker) {
        this.authenticationTracker = authenticationTracker;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        authenticationTracker.handleFailureFromIp(details.getRemoteAddress());
    }
}
