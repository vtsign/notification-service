package tech.vtsign.notificationservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import javax.mail.MessagingException;

public interface UserConsumerService {

    @KafkaListener(topics = "${tech.vtsign.kafka.user-service.register}")
    void consumeMessageRegister(@Payload Object object, @Headers MessageHeaders headers) throws MessagingException;

    @KafkaListener(topics = "${tech.vtsign.kafka.user-service.reset-password}")
    void consumeMessageReset(@Payload Object object, @Headers MessageHeaders headers) throws MessagingException;

    @KafkaListener(topics = "${tech.vtsign.kafka.user-service.notify-common}")
    void consumeMessageCommon(@Payload Object object, @Headers MessageHeaders headers) throws MessagingException;
}
