package com.Hotel_Booking.dto;


import com.Hotel_Booking.entity.Booking;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class BookingResponse {
    private String message;
    private Booking booking;

    public BookingResponse(String message, Booking booking) {
        this.message = message;
        this.booking = booking;
    }


}
