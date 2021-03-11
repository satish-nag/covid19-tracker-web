package com.covid19tracker.web.services.impl;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.covid19tracker.web.constants.AWSConstants;
import com.covid19tracker.web.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@Slf4j
public class EmailServiceImpl implements EmailService {

    /*@Value("${aws.accessKey}")
    public String accessKey;

    @Value("${aws.secretKey}")
    public String secretKey;*/

    AmazonSimpleEmailService simpleEmailService;

    @PostConstruct
    public void init(){
        /*System.setProperty(AWSConstants.AWS_ACCESS_KEY_ID,accessKey);
        System.setProperty(AWSConstants.AWS_SECRET_KEY,secretKey);*/
        simpleEmailService = AmazonSimpleEmailServiceClient.builder().withRegion(Regions.US_EAST_2).build();
    }

    @Override
    public boolean sendEmail(String from, String to, String cc,String bcc,String body,String subject) {
        try {
            SendEmailRequest emailRequest = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(to).withCcAddresses(cc).withBccAddresses(bcc))
                    .withMessage(new Message().withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(body)))
                            .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                    .withSource(from);
            SendEmailResult sendEmailResult = simpleEmailService.sendEmail(emailRequest);
            log.info("Email sent successfully!!!");
            return true;
        }catch (Exception ex){
            log.error("Exception occurred while sending email from ",ex);
            return false;
        }
    }
}
