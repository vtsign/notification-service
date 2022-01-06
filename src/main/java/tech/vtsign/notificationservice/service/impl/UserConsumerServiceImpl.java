package tech.vtsign.notificationservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import tech.vtsign.notificationservice.model.*;
import tech.vtsign.notificationservice.service.UserConsumerService;

import javax.mail.MessagingException;
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

    @Override
    public void consumeMessageRegister(Object object, MessageHeaders headers)
            throws MessagingException {
        ConsumerRecord consumerRecord = (ConsumerRecord) object;
        Activation activation = objectMapper.convertValue(consumerRecord.value(), Activation.class);
        log.info("==== Receive message register from user-service {}", activation);
        Map<String, Object> properties = new HashMap<>();
        properties.put("activationLink", activation.getUrl());
        Mail mail = Mail.builder()
                .from(String.format("%s <%s>", "No Reply VTSign", from))
                .to(activation.getTo())
                .htmlTemplate(new HtmlTemplate("activation", properties))
                .subject("[VTSign] Activation Account")
                .build();
        emailSenderService.sendEmail(mail);
    }

    @Override
    public void consumeMessageReset(Object object, MessageHeaders headers)
            throws MessagingException {
        ConsumerRecord consumerRecord = (ConsumerRecord) object;
        ResetPasswordTransfer resetPasswordTransfer = objectMapper.convertValue(consumerRecord.value(), ResetPasswordTransfer.class);
        log.info("==== Receive message reset password from user-service {}", resetPasswordTransfer);
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

    @Override
    public void consumeMessageCommon(Object object, MessageHeaders headers)
            throws MessagingException {
        ConsumerRecord consumerRecord = (ConsumerRecord) object;
        final ObjectMapper mapper = new ObjectMapper();
        CommonMessage commonMessage = mapper.convertValue(consumerRecord.value(), CommonMessage.class);
        log.info("==== Receive from common message user-service {}", commonMessage);

        Map<String, Object> properties = new HashMap<>();
        properties.put("message", commonMessage.getMessage());
        Mail mail = Mail.builder()
                .from(String.format("%s <%s>", "No Reply VTSign", from))
                .to(commonMessage.getTo())
                .htmlTemplate(new HtmlTemplate("notify_common", properties))
                .subject(String.format("[VTSign] Notification - %s", commonMessage.getTitle()))
                .attachments(commonMessage.getAttachments())
                .build();
        emailSenderService.sendEmail(mail);
    }
}
