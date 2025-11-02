package com.Hotel_Booking.controller;

import com.Hotel_Booking.dto.Response;
import com.Hotel_Booking.entity.Booking;
import com.Hotel_Booking.service.interf.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Create a new booking
     * URL: POST /booking/book-room/{roomId}/{userId}
     */
    @PostMapping("/book-room/{roomId}/{userId}")
    public ResponseEntity<Response> saveBooking(
            @PathVariable Long roomId,
            @PathVariable Long userId,
            @RequestBody Booking bookingRequest
    ) {
        Response response = bookingService.saveBooking(roomId, userId, bookingRequest);
        HttpStatus status = response.getStatusCode() == 200 ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    /**
     * Get all bookings
     * URL: GET /booking/all
     */
    @GetMapping("/all")
    public ResponseEntity<Response> getAllBookings() {
        Response response = bookingService.getAllBookings();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get booking by confirmation code
     * URL: GET /booking/confirmation-code/{confirmationCode}
     */
    @GetMapping("/confirmation-code/{confirmationCode}")
    public ResponseEntity<Response> getBookingByConfirmationCode(
            @PathVariable String confirmationCode
    ) {
        Response response = bookingService.findBookingByConfirmationCode(confirmationCode);
        HttpStatus status = response.getStatusCode() == 200 ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }

    /**
     * Cancel booking by ID
     * URL: DELETE /booking/cancel/{bookingId}
     */
    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<Response> cancelBooking(@PathVariable Long bookingId) {
        Response response = bookingService.cancelBooking(bookingId);
        HttpStatus status = response.getStatusCode() == 200 ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }
}
