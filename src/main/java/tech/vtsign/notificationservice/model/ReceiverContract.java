package tech.vtsign.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverContract {
    private Receiver receiver;

    private Date createdDate;
    private String mailMessage;
    private String mailTitle;
    private String url;
    private String senderName;
}
