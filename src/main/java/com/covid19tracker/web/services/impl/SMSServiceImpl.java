package com.covid19tracker.web.services.impl;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.covid19tracker.web.constants.AWSConstants;
import com.covid19tracker.web.services.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
@Slf4j
public class SMSServiceImpl implements SMSService {

    @Value("${aws.accessKey}")
    public String accessKey;

    @Value("${aws.secretKey}")
    public String secretKey;

    AmazonSNS amazonSNSClient;

    HashMap<String, MessageAttributeValue> messageAttributes;

    @PostConstruct
    public void init(){
        System.setProperty(AWSConstants.AWS_ACCESS_KEY_ID,accessKey);
        System.setProperty(AWSConstants.AWS_SECRET_KEY,secretKey);
        amazonSNSClient = AmazonSNSClient.builder().withRegion(Regions.US_EAST_2).build();
        messageAttributes = new HashMap<>();
        messageAttributes.put("AWS.SNS.SMS.SenderID",new MessageAttributeValue()
                .withStringValue("COVITRK")
                .withDataType("String"));
        messageAttributes.put("AWS.SNS.SMS.SMSType",new MessageAttributeValue()
                .withStringValue("Transactional").withDataType("String"));

    }

    @Override
    public boolean sendSms(String mobileNumber, String message) {
        try {
            PublishResult result = amazonSNSClient.publish(new PublishRequest().withMessageAttributes(messageAttributes)
                    .withMessage(message)
                    .withPhoneNumber(mobileNumber));
            log.info("Message sent successfully, message ID: {}",result.getMessageId());
            return true;
        }catch (Exception ex){
            log.error("Exception occurred while sending sms",ex);
        }
        return false;
    }
}
