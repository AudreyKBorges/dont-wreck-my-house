package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.*;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
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

    public static Host host = new Host("3edda6bc-ab95-49a8-8962-d50b53f84b15","Yearnes"," ","(806) 1783815","3 Nova Trail","Amarillo","TX","79182", BigDecimal.valueOf(Double.parseDouble(String.valueOf(340))),BigDecimal.valueOf(Double.parseDouble(String.valueOf(425))));
    public static Host host2 = new Host("3edda6bc-ab95-49a8-8962-d50b53f84b15","Yearnes",null,"(806) 1783815","3 Nova Trail","Amarillo","TX","79182", BigDecimal.valueOf(Double.parseDouble(String.valueOf(340))),BigDecimal.valueOf(Double.parseDouble(String.valueOf(425))));
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
    void shouldFindReservationsByHostEmail() throws DataException {
        List<Reservation> result = service.findByHost(HostRepositoryTestDouble.HOST);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldUpdateExistingReservation() throws DataException {
        List<Reservation> all = service.findByHost(HostRepositoryTestDouble.HOST);
        Reservation toUpdate = all.get(0);
        toUpdate.setEndDate(LocalDate.of(2022,11,30));

        Result<Reservation> result = service.updateReservation(toUpdate, all);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldDeleteReservation() throws DataException {
        List<Reservation> all = service.findByHost(HostRepositoryTestDouble.HOST);
        Result result = service.deleteReservation(ReservationRepositoryTestDouble.RESERVATION, all);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotMakeReservationWithOverlappingDates() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(0);
        reservation.setHost(HostRepositoryTestDouble.HOST);
        reservation.setStartDate(LocalDate.of(2022,10,31));
        reservation.setEndDate(LocalDate.of(2022,11,12));
        Guest guest = new Guest();
        guest.setId(1);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.valueOf(300));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
    }

    @Test
    void shouldNotAddReservationWithNullHostEmail() throws DataException {
        Reservation reservation = new Reservation();
        Result actual = service.add(reservation);
        reservation.setId(0);
        reservation.setHost(host2);
        reservation.setStartDate(LocalDate.of(2022,10,10));
        reservation.setEndDate(LocalDate.of(2022,10,12));
        Guest guest = new Guest();
        guest.setId(1);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.valueOf(300));

        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals("Host email cannot be null.", actual.getMessages().get(0));
    }
    @Test
    void shouldNotFindByBlankHostEmail() throws DataException {
        Reservation reservation = new Reservation();
        Result actual = service.add(reservation);
        reservation.setId(0);
        reservation.setHost(host);
        reservation.setStartDate(LocalDate.of(2022,10,10));
        reservation.setEndDate(LocalDate.of(2022,10,12));
        Guest guest = new Guest();
        guest.setId(1);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.valueOf(300));

        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals("Host email cannot be null.", actual.getMessages().get(0));
    }
    @Test
    void shouldValidateDuplicateDates() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(0);
        reservation.setHost(HostRepositoryTestDouble.HOST);
        reservation.setStartDate(LocalDate.of(2022,10,31));
        reservation.setEndDate(LocalDate.of(2022,11,5));
        Guest guest = new Guest();
        guest.setId(1);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.valueOf(300));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
    }

    @Test
    void shouldNotUpdateNonExisting() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(999);
        reservation.setHost(HostRepositoryTestDouble.HOST);
        reservation.setStartDate(LocalDate.of(2023,1,10));
        reservation.setEndDate(LocalDate.of(2023,1,12));
        Guest guest = new Guest();
        guest.setId(1);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.valueOf(300));

        List<Reservation> existingReservations = service.findByHost(reservation.getHost());

        Result result = service.updateReservation(reservation, existingReservations);
        assertFalse(result.isSuccess());
    }
    @Test
    void shouldNotAddWithStartDateBeforePresentDay() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(0);
        reservation.setHost(HostRepositoryTestDouble.HOST);
        reservation.setStartDate(LocalDate.of(2022,8,31));
        reservation.setEndDate(LocalDate.now());
        Guest guest = new Guest();
        guest.setId(1);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.valueOf(300));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
    }
    @Test
    void shouldNotDeleteFromDatesInPast() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(0);
        reservation.setHost(HostRepositoryTestDouble.HOST);
        reservation.setStartDate(LocalDate.of(2022,1,10));
        reservation.setEndDate(LocalDate.of(2022,1,12));
        Guest guest = new Guest();
        guest.setId(1);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.valueOf(300));

        List<Reservation> existingReservations = service.findByHost(reservation.getHost());

        Result result = service.deleteReservation(reservation, existingReservations);
        assertFalse(result.isSuccess());
    }

}