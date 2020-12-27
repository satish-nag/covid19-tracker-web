package com.covid19tracker.web.services;

public interface SMSService {
    boolean sendSms(String mobileNumber,String message);
}
