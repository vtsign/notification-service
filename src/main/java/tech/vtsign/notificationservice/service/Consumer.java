package tech.vtsign.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import tech.vtsign.notificationservice.model.User;

import java.io.IOException;


@Service
@Slf4j
public class Consumer {


    @KafkaListener(topics = "final-topic", groupId = "group_one", containerFactory = "kafkaListenerContainerFactory")
    public void consumeUserMessage(@Payload User user, @Headers MessageHeaders headers) throws IOException {
        log.info("received data in Consumer="+ user);

    }
}
