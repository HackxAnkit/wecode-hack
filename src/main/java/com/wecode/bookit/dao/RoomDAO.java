package com.wecode.bookit.dao;

import com.wecode.bookit.domain.Rooms;

import java.util.List;

public interface RoomDAO {
    void addRoom(Rooms room);
    void updateRoom(int roomId, String roomName, int seatingCapacity);
    void deleteRoom(int roomId);
    List<Rooms> getAllRooms();
    List<Rooms> getDefaultRooms();
}
