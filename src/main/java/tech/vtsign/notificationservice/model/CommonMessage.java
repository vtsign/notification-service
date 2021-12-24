package tech.vtsign.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonMessage {
    private String to;
    private String title;
    private String message;
    private List<Attachment> attachments;
}