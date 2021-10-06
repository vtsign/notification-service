package tech.vtsign.notificationservice.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Mail {
    private String from;
    private String to;
    private String subject;
    private HtmlTemplate htmlTemplate;
}