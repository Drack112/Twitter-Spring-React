package com.gmail.drack.broker.producer;

import com.gmail.drack.commons.constants.KafkaTopicConstants;
import com.gmail.drack.commons.event.SendEmailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SendEmailProducer {

    private final KafkaTemplate<String, SendEmailEvent> kafkaTemplate;

    public void sendEmail(SendEmailEvent sendEmailEvent) {
        kafkaTemplate.send(KafkaTopicConstants.SEND_EMAIL_TOPIC, sendEmailEvent);
    }

    public static SendEmailEvent toSendRegistrationEmailEvent(String email, String fullName, String activationCode) {
        return SendEmailEvent.builder()
                .toEmail(email)
                .subject("Registration code")
                .template("registration-template")
                .attributes(Map.of(
                        "fullName", fullName,
                        "registrationCode", activationCode))
                .build();
    }

    public static SendEmailEvent toSendPasswordResetEmailEvent(String email, String fullName, String passwordResetCode) {
        return SendEmailEvent.builder()
                .toEmail(email)
                .subject("Password reset")
                .template("password-reset-template")
                .attributes(Map.of(
                        "fullName", fullName,
                        "passwordResetCode", passwordResetCode))
                .build();
    }
}
