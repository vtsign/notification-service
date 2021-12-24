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
import tech.vtsign.notificationservice.model.*;
import tech.vtsign.notificationservice.service.DocumentConsumerService;
import tech.vtsign.notificationservice.service.EmailSenderService;
import tech.vtsign.notificationservice.service.SMSSenderService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentConsumerServiceImpl implements DocumentConsumerService {
    private final EmailSenderService emailSenderService;
    private final SMSSenderService smsSenderService;
    private final ObjectMapper objectMapper;

    @Value("${spring.mail.username}")
    private String from;

    @KafkaListener(topics = "${tech.vtsign.kafka.document-service.notify-sign}")
    @Override
    public void consumeMessage(@Payload Object object, @Headers MessageHeaders headers)
            throws IOException, MessagingException {
        ConsumerRecord consumerRecord = (ConsumerRecord) object;
        ReceiverContract receiverContract = objectMapper.convertValue(consumerRecord.value(), ReceiverContract.class);
        log.info("==== Receive from document {}", receiverContract);
        Receiver receiver = receiverContract.getReceiver();

        Map<String, Object> properties = new HashMap<>();
        properties.put("signLink", receiverContract.getUrl());
        properties.put("senderName", receiverContract.getSenderName());
        properties.put("mailMessage", receiverContract.getMailMessage());
        properties.put("name", receiver.getName());
        properties.put("privateMessage", receiver.getPrivateMessage());
        Mail mail = Mail.builder()
                .from(String.format("%s via VTSign <%s>", receiverContract.getSenderName(), from))
                .to(receiver.getEmail())
                .htmlTemplate(new HtmlTemplate("invite_sign", properties))
                .subject(String.format("[VTSign] Sign Contract - %s", receiverContract.getMailTitle()))
                .build();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        LocalDateTime dateTime = receiverContract.getCreatedDate() != null
                ? receiverContract.getCreatedDate() : LocalDateTime.now();
        emailSenderService.sendEmail(mail);
        smsSenderService.sendSMS(receiver.getPhone(),
                String.format("%s da moi ban ky mot tai lieu \"%s\" luc %s (UTC) ma bao mat: %s", receiverContract.getSenderName(),
                        receiverContract.getMailTitle(), dateTime.format(formatter), receiver.getKey()));
    }

    @KafkaListener(topics = "${tech.vtsign.kafka.document-service.notify-common}")
    @Override
    public void consumeMessageCommon(@Payload Object object, @Headers MessageHeaders headers)
            throws IOException, MessagingException {
        ConsumerRecord consumerRecord = (ConsumerRecord) object;
        final ObjectMapper mapper = new ObjectMapper();
        CommonMessage commonMessage = mapper.convertValue(consumerRecord.value(), CommonMessage.class);
        log.info("==== Receive from document {}", commonMessage);

        Map<String, Object> properties = new HashMap<>();
        properties.put("message", commonMessage.getMessage());
        Mail mail = Mail.builder()
                .from(String.format("%s <%s>", "No Reply VTSign", from))
                .to(commonMessage.getTo())
                .htmlTemplate(new HtmlTemplate("notify_common", properties))
                .subject(String.format("[VTSign] Contract - %s", commonMessage.getTitle()))
                .attachments(commonMessage.getAttachments())
                .build();
        emailSenderService.sendEmail(mail);
    }
}
