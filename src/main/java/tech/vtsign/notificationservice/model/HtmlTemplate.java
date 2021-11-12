package tech.vtsign.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HtmlTemplate {
    private String template;
    private Map<String, Object> props;
}