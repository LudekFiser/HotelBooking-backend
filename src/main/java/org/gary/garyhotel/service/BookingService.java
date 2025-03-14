package org.gary.garyhotel.service;

import org.gary.garyhotel.exception.ResourceNotFoundException;
import org.gary.garyhotel.model.Booking;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.gary.garyhotel.exception.InvalidBookingRequestException;
import org.gary.garyhotel.model.Booking;
import org.gary.garyhotel.model.Room;
import org.gary.garyhotel.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

        private final BookingRepository bookingRepository;
        private final IRoomService roomService;


        @Override
        public List<Booking> getAllBookings() {
            return bookingRepository.findAll();
        }

    @Override
    public List<Booking> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    public List<Booking> getAllBookingsByRoomId(Long roomId) {
            return bookingRepository.findByRoomId(roomId);
        }

        @Override
        public void cancelBooking(Long bookingId) {
            bookingRepository.deleteById(bookingId);
        }

        @Override
        public String saveBooking(Long roomId, Booking bookingRequest) {
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new InvalidBookingRequestException("Check-in date must come before check-out date");
            }
            Room room = roomService.getRoomById(roomId).get();
            List<Booking> existingBookings = room.getBookings();
            boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
            if (roomIsAvailable) {
                room.addBooking(bookingRequest);
                bookingRepository.save(bookingRequest);
            } else {
                throw new InvalidBookingRequestException("Sorry, This room is not available for the selected dates");
            }
            return bookingRequest.getBookingConfirmationCode();
        }

        @Override
        public Booking findByBookingConfirmationCode(String confirmationCode) {
            return bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new ResourceNotFoundException("Booking with confirmation code: " + confirmationCode + " not found"));
        }

        private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
            return existingBookings.stream()
                    .noneMatch(existingBooking ->
                            bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                    || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                    || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                    && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                    || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                    && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                    || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                    && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                    || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                    && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                    || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                    && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                    );
        }
    }
