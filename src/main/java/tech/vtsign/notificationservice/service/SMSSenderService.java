package tech.vtsign.notificationservice.service;

public interface SMSSenderService {
    boolean sendSMS(String to, String content);
}
