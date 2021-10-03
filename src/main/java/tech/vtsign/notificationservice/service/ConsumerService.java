package tech.vtsign.notificationservice.service;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import tech.vtsign.notificationservice.model.User;

import javax.mail.MessagingException;
import java.io.IOException;

public interface ConsumerService {
    void consumeUserMessage(@Payload User user, @Headers MessageHeaders headers) throws IOException, MessagingException;
}
