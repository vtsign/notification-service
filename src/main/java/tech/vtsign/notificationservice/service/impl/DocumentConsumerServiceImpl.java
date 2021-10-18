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
import tech.vtsign.notificationservice.model.InfoMailReceiver;
import tech.vtsign.notificationservice.model.Mail;
import tech.vtsign.notificationservice.service.DocumentConsumerService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DocumentConsumerServiceImpl implements DocumentConsumerService {
    private final EmailSenderServiceImpl emailSenderService;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${tech.vtsign.hostname}")
    private String hostname;

    public DocumentConsumerServiceImpl(EmailSenderServiceImpl emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @KafkaListener(topics = "${tech.vtsign.kafka.document-service.notify-sign}")
    @Override
    public void consumeMessage(@Payload Object object, @Headers MessageHeaders headers) throws IOException, MessagingException {
        ConsumerRecord consumerRecord = (ConsumerRecord) object;
        final ObjectMapper mapper = new ObjectMapper();
        InfoMailReceiver infoMailReceiver = mapper.convertValue(consumerRecord.value(), InfoMailReceiver.class);
        log.info("==== Receive from document {}", infoMailReceiver);
        Map<String, Object> properties = new HashMap<>();
        properties.put("signLink", infoMailReceiver.getUrl());
        properties.put("nameSender", infoMailReceiver.getNameSender());
        Mail mail = Mail.builder()
                .from(String.format("%s via VTSign <%s>", infoMailReceiver.getNameSender(), from))
                .to(infoMailReceiver.getEmail())
                .htmlTemplate(new HtmlTemplate("email_launch", properties))
                .subject("[VTSign] Sign Document")
                .build();
        emailSenderService.sendEmail(mail);
    }
}
