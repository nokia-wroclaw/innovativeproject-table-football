package com.tablefootbal.server.security.bruteforce.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tablefootbal.server.security.bruteforce.LoginTrackerService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IpBlockFilter extends GenericFilter {

    private LoginTrackerService loginTrackerService;
    private MessageSource messageSource;
    private ObjectMapper objectMapper = new ObjectMapper();

    public IpBlockFilter(LoginTrackerService loginTrackerService, MessageSource messageSource) {
        this.loginTrackerService = loginTrackerService;
        this.messageSource = messageSource;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String remoteAddress = request.getRemoteAddr();
        String authorizationHeader = ((HttpServletRequest) request).getHeader("Authorization");
        if (authorizationHeader != null && loginTrackerService.isIpBlocked(remoteAddress)) {
            Map<String, Object> responseValues = new HashMap<>();

            responseValues.put("errorMessage", messageSource.getMessage(
                    "exception.ip_blocked",
                    new Object[]{remoteAddress},
                    Locale.getDefault()));

            responseValues.put("status", HttpStatus.UNAUTHORIZED.value());

            String message = objectMapper.writeValueAsString(responseValues);
            ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());

            response.getOutputStream().println(message);
        } else {
            chain.doFilter(request, response);
        }

    }
}
