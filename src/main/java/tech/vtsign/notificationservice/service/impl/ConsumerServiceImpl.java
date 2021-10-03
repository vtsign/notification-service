package tech.vtsign.notificationservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import tech.vtsign.notificationservice.model.Mail;
import tech.vtsign.notificationservice.model.User;
import tech.vtsign.notificationservice.service.ConsumerService;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

    private final EmailSenderServiceImpl emailSenderService;

    @Value("${spring.mail.username}")
    private String from;

    @KafkaListener(topics = "final-topic", groupId = "group_one", containerFactory = "kafkaListenerContainerFactory")
    @Override
    public void consumeUserMessage(@Payload User user, @Headers MessageHeaders headers) throws MessagingException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("activationLink", String.format("https://vtsign.tech/activation/%s", user.getId()));
        Mail mail = Mail.builder()
                .from(String.format("%s <%s>", "No Reply VTSign", from))
                .to(user.getEmail())
                .htmlTemplate(new Mail.HtmlTemplate("email_activation", properties))
                .subject("[VTSign] Activation Account")
                .build();
        emailSenderService.sendEmail(mail);
    }

}
