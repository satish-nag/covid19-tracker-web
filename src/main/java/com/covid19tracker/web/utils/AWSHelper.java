package com.covid19tracker.web.utils;

import com.covid19tracker.web.services.EmailService;
import com.covid19tracker.web.services.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AWSHelper {

    @Autowired
    SMSService smsService;

    @Autowired
    EmailService emailService;

    public boolean sendSMS(String mobileNumber, String message) {
        return smsService.sendSms(mobileNumber,message);
    }

    public boolean sendEmail(String from,String to,String cc,String bcc,String body,String subject){
        return emailService.sendEmail(from,to,cc,bcc,body,subject);
    }
}
