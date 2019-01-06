package com.tablefootbal.server.security.bruteforce;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@PropertySource("classpath:brute_force.properties")
public class LoginTrackerService {

    @Value("${brute_force.max_failed_attempts}")
    private int MAX_ATTEMPTS;

    private Map<String, Integer> attemptsTracker;

    public LoginTrackerService() {
        this.attemptsTracker = new HashMap<>();
    }

    public void handleFailureFromIp(String remoteAddress) {
        Integer failedAttempts = attemptsTracker.get(remoteAddress);
        if (null == failedAttempts) {
            attemptsTracker.put(remoteAddress, 1);
        } else {
            attemptsTracker.put(remoteAddress, ++failedAttempts);
        }
    }

    public void handleSuccessFromIp(String remoteAddress) {
        attemptsTracker.remove(remoteAddress);
    }

    public boolean isIpBlocked(String remoteAddress) {
        Integer failedAttempts = attemptsTracker.get(remoteAddress);
        return (null != failedAttempts && failedAttempts >= MAX_ATTEMPTS);
    }

    @Scheduled(fixedRateString = "${brute_force.reset_attempts_millis}")
    public void resetFailedAttempts() {
        this.attemptsTracker = new HashMap<>();
    }
}
