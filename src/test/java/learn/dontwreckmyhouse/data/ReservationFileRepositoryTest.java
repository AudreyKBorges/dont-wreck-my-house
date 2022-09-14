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
    static final String SEED_FILE_PATH = "./data/reservation-seed.csv";
    static final String TEST_FILE_PATH = "./data/reservation-test.csv";

    private final ReservationFileRepository repository = new ReservationFileRepository(TEST_FILE_PATH);

    private final String hostId = "3edda6bc-ab95-49a8-8962-d50b53f84b15";
    private final int RESERVATION_COUNT = 0;

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
        Host host = new Host();
        host.setId(hostId);
        List<Reservation> actual = repository.findByHost(host);
        assertEquals(RESERVATION_COUNT, actual.size());
    }

    @Test
    void shouldUpdateReservation() {

    }
}