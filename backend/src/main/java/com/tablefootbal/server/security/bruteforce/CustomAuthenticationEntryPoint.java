package com.tablefootbal.server.security.bruteforce;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        Map<String, Object> responseValues = new HashMap<>();
        responseValues.put("errorMessage", authException.getMessage());
        responseValues.put("status", HttpStatus.UNAUTHORIZED.value());

        String message = objectMapper.writeValueAsString(responseValues);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getOutputStream().println(message);
    }
}
