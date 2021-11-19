package tech.vtsign.notificationservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import tech.vtsign.notificationservice.model.HtmlTemplate;
import tech.vtsign.notificationservice.model.Mail;
import tech.vtsign.notificationservice.model.User;
import tech.vtsign.notificationservice.service.UserConsumerService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class UserConsumerServiceImpl implements UserConsumerService {

    private final EmailSenderServiceImpl emailSenderService;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${tech.vtsign.hostname}")
    private String hostname;

    public UserConsumerServiceImpl(EmailSenderServiceImpl emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @KafkaListener(topics = "${tech.vtsign.kafka.user-service.register}")
    @Override
    public void consumeMessage(@Payload Object object, @Headers MessageHeaders headers) throws IOException, MessagingException {
        ConsumerRecord consumerRecord = (ConsumerRecord) object;
        final ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(consumerRecord.value(), User.class);
        log.info("==== Receive message register from user-service {}", user);
        Map<String, Object> properties = new HashMap<>();
        properties.put("activationLink", String.format("%s/activation/%s", hostname, user.getId()));
        Mail mail = Mail.builder()
                .from(String.format("%s <%s>", "No Reply VTSign", from))
                .to(user.getEmail())
                .htmlTemplate(new HtmlTemplate("activation", properties))
                .subject("[VTSign] Activation Account")
                .build();
        emailSenderService.sendEmail(mail);
    }

}
