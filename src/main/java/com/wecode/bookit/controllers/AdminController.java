package com.wecode.bookit.controllers;

import com.wecode.bookit.dto.CreateAmenityDto;
import com.wecode.bookit.dto.CreateMeetingRoomDto;
import com.wecode.bookit.dto.MeetingRoomDto;
import com.wecode.bookit.dto.UpdateAmenityDto;
import com.wecode.bookit.dto.UpdateMeetingRoomDto;
import com.wecode.bookit.entity.Amenity;
import com.wecode.bookit.services.AmenityService;
import com.wecode.bookit.services.MeetingRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final MeetingRoomService meetingRoomService;
    private final AmenityService amenityService;

    public AdminController(MeetingRoomService meetingRoomService, AmenityService amenityService) {
        this.meetingRoomService = meetingRoomService;
        this.amenityService = amenityService;
    }

    @PostMapping("/createRoom")
    public ResponseEntity<MeetingRoomDto> createRoom(@RequestBody CreateMeetingRoomDto createRoomDto) {
        try{
            MeetingRoomDto room = meetingRoomService.createRoom(createRoomDto);
            room.setStatusCode(HttpStatus.CREATED.value());
            room.setMessage("Room created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
        }
        catch (Exception e){
        MeetingRoomDto errorResponse = new MeetingRoomDto();
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    }

    @PutMapping("/updateRoom")
    public ResponseEntity<MeetingRoomDto> updateRoom(@RequestBody UpdateMeetingRoomDto updateRoomDto) {
        try{
            MeetingRoomDto room = meetingRoomService.updateRoom(updateRoomDto);
            room.setStatusCode(HttpStatus.CREATED.value());
            room.setMessage("Room updated successfully");
        return ResponseEntity.ok(room);
        }
        catch (Exception e){
        MeetingRoomDto errorResponse = new MeetingRoomDto();
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    }

    @GetMapping("/getRoomById/{roomId}")
    public ResponseEntity<MeetingRoomDto> getRoomById(@PathVariable UUID roomId) {
        try {
            MeetingRoomDto room = meetingRoomService.getRoomById(roomId);
            room.setStatusCode(HttpStatus.OK.value());
            room.setMessage("Room fetched successfully");
            return ResponseEntity.ok(room);
        }
        catch (Exception e)
        {
            MeetingRoomDto errorResponse = new MeetingRoomDto();
            errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/getAllRoom")
    public ResponseEntity<List<MeetingRoomDto>> getAllRooms() {
        try {
            List<MeetingRoomDto> rooms = meetingRoomService.getAllRooms();
            return ResponseEntity.ok(rooms);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable UUID roomId) {
        try {
            meetingRoomService.deleteRoom(roomId);
            return ResponseEntity.ok("Room deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/addAmenitie")
    public ResponseEntity<Amenity> createAmenity(@RequestBody CreateAmenityDto createAmenityDto) {
        try {
            Amenity amenity = amenityService.createAmenity(createAmenityDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(amenity);
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/updateAmenitie")
    public ResponseEntity<Amenity> updateAmenity(@RequestBody UpdateAmenityDto updateAmenityDto) {
        try {
            Amenity amenity = amenityService.updateAmenity(updateAmenityDto);
            return ResponseEntity.ok(amenity);
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @GetMapping("/getAmenitieById/{amenityId}")
    public ResponseEntity<Amenity> getAmenityById(@PathVariable UUID amenityId) {
        try {
            Amenity amenity = amenityService.getAmenityById(amenityId);
            return ResponseEntity.ok(amenity);
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/getAllAmenities")
    public ResponseEntity<List<Amenity>> getAllAmenities() {
        try {
            List<Amenity> amenities = amenityService.getAllAmenities();
            return ResponseEntity.ok(amenities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/amenities/{amenityId}")
    public ResponseEntity<String> deleteAmenity(@PathVariable UUID amenityId) {
        try {
            amenityService.deleteAmenity(amenityId);
            return ResponseEntity.ok("Amenity deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
