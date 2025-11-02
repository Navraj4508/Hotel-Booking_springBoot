package com.Hotel_Booking.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    private String userName;
    private String userEmail;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    // Getters and setters
}
