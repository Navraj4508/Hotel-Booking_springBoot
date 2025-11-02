package com.Hotel_Booking.controller;


import com.Hotel_Booking.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order/{bookingId}")
    public ResponseEntity<String> createOrder(@PathVariable Long bookingId,
                                              @RequestParam Double amount) {
        try {
            String order = paymentService.createOrder(amount, bookingId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> data) {
        String orderId = data.get("order_id");
        String paymentId = data.get("payment_id");
        String signature = data.get("signature");
        String message = paymentService.verifyPayment(orderId, paymentId, signature);
        return ResponseEntity.ok(message);
    }
}

