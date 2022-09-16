package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.*;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Reservation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {
    ReservationService service = new ReservationService(
            new ReservationRepositoryTestDouble(),
            new GuestRepositoryTestDouble(),
            new HostRepositoryTestDouble());

    @Test
    void shouldAddReservation() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setHost(HostRepositoryTestDouble.HOST);
        reservation.setStartDate(LocalDate.of(2022,11,11));
        reservation.setEndDate(LocalDate.of(2022,11,12));
        reservation.setGuest(GuestRepositoryTestDouble.GUEST);
        reservation.setTotal(BigDecimal.valueOf(400));

        Result actual = service.add(reservation);

        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldFindReservationsByHost() throws DataException {
        List<Reservation> result = service.findByHost(HostRepositoryTestDouble.HOST);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldUpdateReservation() throws DataException {
        List<Reservation> all = service.findByHost(HostRepositoryTestDouble.HOST);
        Reservation toUpdate = all.get(0);
        toUpdate.setEndDate(LocalDate.of(2022,11,30));

        Result<Reservation> result = service.updateReservation(toUpdate);

        assertTrue(result.isSuccess());
    }

//    @Test
//    void shouldDeleteReservation() {
//        boolean actual = service.deleteReservation(ReservationRepository.RESERVATION);
//        assertTrue(actual);
//    }

    @Test
    void shouldNotMakeReservationWithAddOverlappingDates() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(0);
        reservation.setHost(HostRepositoryTestDouble.HOST);
        reservation.setStartDate(LocalDate.of(2022,10,10));
        reservation.setEndDate(LocalDate.of(2022,10,12));
        Guest guest = new Guest();
        guest.setId(1);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.valueOf(300));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
    }

    @Test
    void shouldNotAddReservationWithNullHost() throws DataException {
        Reservation reservation = null;
        Result actual = service.add(reservation);

        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals("Reservation entry cannot be null.", actual.getMessages().get(0));
    }
}