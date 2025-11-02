package com.Hotel_Booking.service.interf;

import com.Hotel_Booking.dto.LoginRequest;
import com.Hotel_Booking.dto.Response;
import com.Hotel_Booking.entity.User;

import java.util.Optional;

public interface UserService {

    Response register(User user);

    Response getAllUsers();

    Optional<Response> getUserBookingHistory(Long userId);

    Response deleteUser(Long userId) throws Exception;

    Response getUserById(Long userId);

    Response getMyInfo(String email);
}
