package tech.vtsign.notificationservice.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class Receiver {

    @KafkaListener(topics = "user_service",groupId = "group_id")
    public void receive(User user){
        log.info("received car='{}'", user.toString());

    }
}
