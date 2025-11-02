package com.Hotel_Booking.entity;


import com.Hotel_Booking.utils.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private String paymentId;
    private String signature;
    private Double amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private Status status; // CREATED, SUCCESS, FAILED
    private Long bookingId; // Link to your booking

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate = new Date();


}

