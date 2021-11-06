package tech.vtsign.notificationservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.vtsign.notificationservice.service.SMSSenderService;

import java.util.Date;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class SMSSenderServiceTests {

    @Autowired
    private SMSSenderService smsSenderService;

    @Test
    public void sentSMSSuccessToValidPhone() {
        String phone = "+84975503208";
        String message = String.format("Message sent from VTSign at %s", new Date());

        boolean status = smsSenderService.sendSMS(phone, message);
        assertThat(status).isTrue();
    }

    @Test
    public void sentSMSFailToValidPhone() {
        String phone = "abcdefgh";
        String message = String.format("Message sent from VTSign at %s", new Date());

        boolean status = smsSenderService.sendSMS(phone, message);
        assertThat(status).isFalse();
    }


}
