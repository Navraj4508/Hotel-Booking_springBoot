package com.Hotel_Booking.service.interf;

import com.Hotel_Booking.dto.Response;
import com.Hotel_Booking.entity.Booking;

public interface BookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);

}
