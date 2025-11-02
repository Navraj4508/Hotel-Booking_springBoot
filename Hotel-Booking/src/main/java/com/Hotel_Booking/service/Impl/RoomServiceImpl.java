package com.Hotel_Booking.service.Impl;


import com.Hotel_Booking.dto.Response;
import com.Hotel_Booking.dto.RoomDTO;
import com.Hotel_Booking.entity.Room;
import com.Hotel_Booking.exception.OurException;
import com.Hotel_Booking.repository.BookingRepository;
import com.Hotel_Booking.repository.RoomRepository;
import com.Hotel_Booking.service.AwsService;
import com.Hotel_Booking.service.interf.RoomService;
import com.Hotel_Booking.utils.Utils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;

    private BookingRepository bookingRepository;

    private AwsService awsService;


    public RoomServiceImpl(RoomRepository roomRepository, BookingRepository bookingRepository,AwsService awsService) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.awsService = awsService;
    }


    @Override
    public Response addNewRoom(RoomDTO roomDTO) {
        Response response = new Response();

        try {
            // ✅ Basic field validation
            if (roomDTO.getRoomType() == null || roomDTO.getRoomType().isBlank() ||
                    roomDTO.getRoomPrice() == null || roomDTO.getRoomDescription() == null ||
                    roomDTO.getRoomDescription().isBlank()) {

                response.setStatusCode(400);
                response.setMessage("Please provide values for all required fields.");
                return response;
            }

            // ✅ Create entity from DTO
            Room room = new Room();
            room.setRoomPhotoUrl(roomDTO.getRoomPhotoUrl());
            room.setRoomType(roomDTO.getRoomType());
            room.setRoomPrice(roomDTO.getRoomPrice());
            room.setRoomDescription(roomDTO.getRoomDescription());

            // ✅ Save room in DB
            Room savedRoom = roomRepository.save(room);

            // ✅ Map saved entity to DTO
            RoomDTO savedRoomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);

            response.setStatusCode(201);
            response.setMessage("Room added successfully!");
            response.setRoom(savedRoomDTO);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage("Error: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {

        Response response = new Response();

        try{
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room "+e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {

        Response response = new Response();

        try{
            roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not Found"));
            roomRepository.deleteById(roomId);

            response.setStatusCode(200);
            response.setMessage("Successful");

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room "+e.getMessage());
        }

        return response;
    }

    @Override
    public Response updateRoom(Long roomId,
                               String description,
                               String roomType,
                               BigDecimal roomPrice,
                               MultipartFile photo) {

        Response response = new Response();

        try{
            String imageUrl = null;
            if(photo != null && !photo.isEmpty()){
                 imageUrl = awsService.saveImageToS3(photo);
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not Found!"));
            if(roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if(description != null) room.setRoomDescription(description);
            if(imageUrl != null) room.setRoomPhotoUrl(imageUrl);

            Room updateRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updateRoom);

            response.setStatusCode(200);
            response.setMessage("successsful");
            response.setRoom(roomDTO);

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room "+e.getMessage());
        }

        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {

        Response response = new Response();

        try{
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not Found"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusBookings(room);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);

        }catch(OurException e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room "+e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAvailableRoomsByDataAndType(LocalDate checkInDate,
                                                   LocalDate checkOutDate,
                                                   String roomType) {

        Response response = new Response();

        try{
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate,
                                                checkOutDate,roomType);
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room "+e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();

        try {

            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room "+ e.getMessage());
        }

        return response;
    }
}
