package tech.vtsign.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResetPasswordTransfer {
    private String to;
    private String url;
    private LocalDateTime expireAt;
}
