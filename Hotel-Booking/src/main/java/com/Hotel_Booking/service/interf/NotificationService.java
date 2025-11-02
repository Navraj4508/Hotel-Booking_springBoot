package com.Hotel_Booking.service.interf;


public interface NotificationService {
    void sendBookingConfirmation(String toEmail, String customerName, String bookingDetails);
    void sendCancellationNotice(String toEmail, String bookingId);
}

