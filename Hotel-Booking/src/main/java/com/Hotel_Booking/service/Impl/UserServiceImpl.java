package com.Hotel_Booking.service.Impl;


import com.Hotel_Booking.dto.LoginRequest;
import com.Hotel_Booking.dto.Response;
import com.Hotel_Booking.dto.UserDTO;
import com.Hotel_Booking.entity.User;
import com.Hotel_Booking.exception.OurException;
import com.Hotel_Booking.repository.UserRepository;
import com.Hotel_Booking.service.interf.UserService;
import com.Hotel_Booking.utils.Utils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private Utils utils;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, Utils utils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.utils = utils;
    }

    @Override
    public Response register(User user) {
        Response response = new Response();

        try {
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new OurException(user.getEmail() + "Already Exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Registration " + e.getMessage());

        }
        return response;
    }


    @Override
    public Response getAllUsers() {
        Response response = new Response();

        try{
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = utils.mapUserListEntityToUserListDTO(userList);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUserList(userDTOList);


        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users "+ e.getMessage());
        }

        return response;
    }

    @Override
    public Optional<Response> getUserBookingHistory(Long userId) {

        Response response = new Response();

        try{
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not Found!"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("User Displayed");
            response.setUser(userDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting id: "+userId+ " "+ e.getMessage());
        }
        return Optional.of(response);
    }

    @Override
    public Response deleteUser(Long userId) {
        Response response = new Response();

        try {
            userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException("User not Found!"));
            userRepository.deleteById(Long.valueOf(userId));

            response.setStatusCode(200);
            response.setMessage("User is deleted which userId: " + userId);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting id: "+userId+ " "+ e.getMessage());
        }



        return response;
    }

    @Override
    public Response getUserById(Long userId) {

        Response response = new Response();

        try{
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException("User not found"));
            UserDTO userDTO = utils.mapUserEntityToUserDTO(user);

            response.setStatusCode(200);
            response.setMessage("User details which userId: "+ userId);
            response.setUser(userDTO);
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return  response;
    }

    @Override
    public Response getMyInfo(String email) {

        Response response = new Response();

        try{
            User user = userRepository.findByEmail(email).orElseThrow(() -> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());

        }
        return response;
    }
}
