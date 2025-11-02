package com.Hotel_Booking.service.Impl;



import com.Hotel_Booking.service.interf.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {


    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name}")
    private String appName;

    @Autowired
    public NotificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async // make sending email non-blocking
    @Override
    public void sendBookingConfirmation(String toEmail, String customerName, String bookingDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Booking Confirmation - " + appName);
        message.setText("Dear " + customerName + ",\n\n" +
                "Your booking was successfully confirmed.\n" +
                "Booking Details:\n" + bookingDetails + "\n\n" +
                "Thank you for choosing our hotel!\n" +
                "Best regards,\n" + appName + " Team");

        mailSender.send(message);
    }

    @Async
    @Override
    public void sendCancellationNotice(String toEmail, String bookingId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Booking Cancellation - " + appName);
        message.setText("Dear Customer,\n\n" +
                "Your booking with ID: " + bookingId + " has been cancelled successfully.\n\n" +
                "If this was not you, please contact our support.\n\n" +
                "Regards,\n" + appName + " Team");

        mailSender.send(message);
    }
}

