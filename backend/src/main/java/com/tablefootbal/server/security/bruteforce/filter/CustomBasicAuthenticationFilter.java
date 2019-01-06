package com.tablefootbal.server.security.bruteforce.filter;

import com.tablefootbal.server.security.bruteforce.LoginTrackerService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private LoginTrackerService loginTrackerService;

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        String remoteAddress = request.getRemoteAddr();
        loginTrackerService.handleFailureFromIp(remoteAddress);
        super.onUnsuccessfulAuthentication(request, response, failed);
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        String remoteAddress = request.getRemoteAddr();
        loginTrackerService.handleSuccessFromIp(remoteAddress);
        super.onSuccessfulAuthentication(request, response, authResult);
    }

    public CustomBasicAuthenticationFilter(
            AuthenticationManager authenticationManager,
            AuthenticationEntryPoint authenticationEntryPoint,
            LoginTrackerService loginTrackerService) {
        super(authenticationManager, authenticationEntryPoint);
        this.loginTrackerService = loginTrackerService;
    }
}
