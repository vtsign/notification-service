package tech.vtsign.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Mail {
    private String from;
    private String to;
    private String subject;
    private HtmlTemplate htmlTemplate;
    private List<Attachment> attachments;
}