package tech.vtsign.notificationservice.model;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HtmlTemplate {
    private String template;
    private Map<String, Object> props;
}