package com.covid19tracker.web.services;

public interface EmailService {
    boolean sendEmail(String from,String to,String cc,String bcc,String body,String subject);
}
