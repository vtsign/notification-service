package tech.vtsign.notificationservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import tech.vtsign.notificationservice.model.Activation;
import tech.vtsign.notificationservice.model.HtmlTemplate;
import tech.vtsign.notificationservice.model.Mail;
import tech.vtsign.notificationservice.model.ResetPasswordTransfer;
import tech.vtsign.notificationservice.service.UserConsumerService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserConsumerServiceImpl implements UserConsumerService {

    private final EmailSenderServiceImpl emailSenderService;
    private final ObjectMapper objectMapper;

    @Value("${spring.mail.username}")
    private String from;

    @KafkaListener(topics = "${tech.vtsign.kafka.user-service.register}")
    @Override
    public void consumeMessage(@Payload Object object, @Headers MessageHeaders headers) throws IOException, MessagingException {
        ConsumerRecord consumerRecord = (ConsumerRecord) object;
        Activation activation = objectMapper.convertValue(consumerRecord.value(), Activation.class);
        log.info("==== Receive message register from user-service {}", activation);
        Map<String, Object> properties = new HashMap<>();
//        properties.put("activationLink", String.format("%s/activation/%s", hostname, user.getId()));
        properties.put("activationLink", activation.getUrl());
        Mail mail = Mail.builder()
                .from(String.format("%s <%s>", "No Reply VTSign", from))
                .to(activation.getTo())
                .htmlTemplate(new HtmlTemplate("activation", properties))
                .subject("[VTSign] Activation Account")
                .build();
        emailSenderService.sendEmail(mail);
    }

    @KafkaListener(topics = "${tech.vtsign.kafka.user-service.reset-password}")
    @Override
    public void consumeMessageReset(Object object, MessageHeaders headers) throws IOException, MessagingException {
        ConsumerRecord consumerRecord = (ConsumerRecord) object;
        ResetPasswordTransfer resetPasswordTransfer = objectMapper.convertValue(consumerRecord.value(), ResetPasswordTransfer.class);
        log.info("==== Receive message register from user-service {}", resetPasswordTransfer);
        Map<String, Object> properties = new HashMap<>();
        properties.put("link", resetPasswordTransfer.getUrl());
        properties.put("email", resetPasswordTransfer.getTo());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        properties.put("expiredAt", resetPasswordTransfer.getExpireAt().format(formatter));
        Mail mail = Mail.builder()
                .from(String.format("%s <%s>", "No Reply VTSign", from))
                .to(resetPasswordTransfer.getTo())
                .htmlTemplate(new HtmlTemplate("reset_password", properties))
                .subject("[VTSign] Reset Password")
                .build();
        emailSenderService.sendEmail(mail);
    }
}
