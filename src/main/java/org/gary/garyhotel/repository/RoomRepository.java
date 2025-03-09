package org.gary.garyhotel.repository;

import org.gary.garyhotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();

    // selecting all rooms with roomType, and adding the room following the requested dates of guest
    @Query("SELECT r FROM Room r " +
            " WHERE r.roomType LIKE %:roomType% " +
            " AND r.id NOT IN (" +
            " SELECT b.room.id FROM Booking b " +
            " WHERE ((b.checkInDate <= :checkOutDate) AND (b.checkOutDate >= :checkInDate))" +
            ")")

    List<Room> findAvailableRoomsByDatesAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
