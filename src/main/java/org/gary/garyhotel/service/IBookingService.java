package org.gary.garyhotel.service;

import org.gary.garyhotel.model.Booking;

import java.util.List;


public interface IBookingService {
    void cancelBooking(Long bookingId);

    String saveBooking(Long roomId, Booking bookingRequest);

    Booking findByBookingConfirmationCode(String confirmationCode);

    List<Booking> getAllBookings();
}
