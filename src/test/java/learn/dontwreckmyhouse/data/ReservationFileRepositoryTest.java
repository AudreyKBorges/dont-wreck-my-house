package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {
    static final String SEED_FILE_PATH = "./data/reservations-data-test/reservations-seed-2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    static final String TEST_FILE_PATH = "./data/reservations-data-test/reservations-test-2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";

    static final String TEST_DIR_PATH = "./data/reservations-data-test";

    private final ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);

    private final String hostId = "2e72f86c-b8fe-4265-b4f1-304dea8762db";

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldAddReservation() throws DataException {
        Host host = new Host();
        host.setId(hostId);

        Reservation reservation = new Reservation();
        reservation.setId(0);
        reservation.setStartDate(LocalDate.of(2022,10,31));
        reservation.setStartDate(LocalDate.of(2022,11,5));
        reservation.setHost(host);

        Guest guest = new Guest();
        guest.setId(1);
        reservation.setTotal(BigDecimal.valueOf(300));
        reservation = repository.add(reservation);
        reservation.setGuest(guest);

        assertEquals(1, reservation.getId());
    }

    @Test
    void shouldFindReservationsByHost() {
        Host host = new Host(hostId, "Test", "test@gmail.com", "3339997777", "123 Main St", "Los Angeles", "CA", "91605", BigDecimal.valueOf(425), BigDecimal.valueOf(499));
        List<Reservation> actual = repository.findByHost(host);
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldUpdateReservation() throws DataException {
        Host host = new Host();
        host.setId(hostId);

        Reservation reservation = repository.findByHost(hostId);
        reservation.setId(0);
        reservation.setStartDate(LocalDate.of(2022,12,15));
        reservation.setStartDate(LocalDate.of(2022,12,18));
        reservation.setHost(host);

        Guest guest = new Guest();
        guest.setId(50);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.valueOf(355));
        boolean result = repository.updateReservation(reservation);

        assertTrue(result);

        assertEquals(1, reservation.getId());
        assertEquals(LocalDate.of(2022,12,15), reservation.getStartDate());
        assertEquals(LocalDate.of(2022,12,18), reservation.getEndDate());
        assertEquals(200, reservation.getGuest().getId());
        assertEquals(BigDecimal.valueOf(355), reservation.getTotal());
    }

    @Test
    void shouldDeleteReservation() {
        boolean result = repository.deleteReservation();
        assertTrue(result);

        List<Reservation> all = repository.findByHost();

        assertEquals(1, all.size());

        Reservation reservation = repository.findById(1, hostId);

        assertNull(reservation);
    }
}