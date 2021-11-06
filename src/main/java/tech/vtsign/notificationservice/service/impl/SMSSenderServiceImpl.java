package tech.vtsign.notificationservice.service.impl;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.vtsign.notificationservice.service.SMSSenderService;

@Service
@Slf4j
public class SMSSenderServiceImpl implements SMSSenderService {
    @Value("${tech.vtsign.twilio.account-sid}")
    private String accountSid;
    @Value("${tech.vtsign.twilio.auth-token}")
    private String authToken;
    @Value("${tech.vtsign.twilio.messaging-service-sid}")
    private String messagingServiceSid;

    @Override
    public boolean sendSMS(String to, String content) {
        Twilio.init(accountSid, authToken);
        try {
            Message message = Message.creator(
                            new PhoneNumber(to),
                            messagingServiceSid,
                            content)
                    .create();
            log.info("Message sent to {}: {}", to, message.getSid());
            return true;
        } catch (ApiException e) {
            log.error("Error while sending SMS", e);
            return false;
        }
    }
}
