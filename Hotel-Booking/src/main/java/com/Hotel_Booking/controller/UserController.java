package com.Hotel_Booking.controller;


import com.Hotel_Booking.dto.Response;
import com.Hotel_Booking.service.interf.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllUsers() {
        Response response = userService.getAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/history/{userId}")
    public ResponseEntity<Optional<Response>> getUserBookingHistory(@PathVariable Long userId) {
        Optional<Response> response = userService.getUserBookingHistory(userId);
        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/deleteId/{userId}")
    public ResponseEntity<Response> deleteByUserId(@PathVariable Long userId) throws Exception {
        Response response = userService.deleteUser(userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/getId/{userId}")
    public ResponseEntity<Response> getUserById(@PathVariable("userId") Long userId) {
        Response response = userService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
