package com.Hotel_Booking.service;


import com.Hotel_Booking.entity.Payment;
import com.Hotel_Booking.repository.PaymentRepository;
import com.Hotel_Booking.utils.Status;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.apache.logging.log4j.status.StatusData;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;


    private PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public String createOrder(Double amount, Long bookingId) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "booking_" + bookingId);

        Order order = client.orders.create(orderRequest);

        // Save payment record
        Payment payment = new Payment();
        payment.setOrderId(order.get("id"));
        payment.setAmount(amount);
        payment.setCurrency("INR");
        payment.setStatus(Status.CREATED);
        payment.setBookingId(bookingId);
        paymentRepository.save(payment);

        return order.toString(); // Send this to frontend
    }

    public String verifyPayment(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;
            boolean isValid = Utils.verifySignature(payload, signature, keySecret);

            Payment payment = paymentRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            if (isValid) {
                payment.setStatus(Status.SUCCESS);
                payment.setPaymentId(paymentId);
                payment.setSignature(signature);
                paymentRepository.save(payment);
                return "Payment verified successfully.";
            } else {
                payment.setStatus(Status.FAILED);
                paymentRepository.save(payment);
                return "Payment verification failed!";
            }

        } catch (Exception e) {
            return "Error verifying payment: " + e.getMessage();
        }
    }
}


