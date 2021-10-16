package tech.vtsign.notificationservice.service;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import javax.mail.MessagingException;
import java.io.IOException;

public interface DocumentConsumerService {
    void consumeMessage(@Payload Object object, @Headers MessageHeaders headers) throws IOException, MessagingException;
}
