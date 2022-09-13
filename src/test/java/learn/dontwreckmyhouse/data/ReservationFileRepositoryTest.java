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

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

//    @Test
//    void shouldAddReservation() throws DataException {
//        Reservation reservation = new Reservation(1, LocalDate.of(2022, 9, 14),
//                LocalDate.of(2022, 9, 20),
//                new Guest(1, "John", "Smith", "johnsmith@gmail.com", "NY"),
//                new Host("1", "Maria", "Garcia", "5123333030", "123 6th Street", "Austin", "TX", "78701", BigDecimal.valueOf(225.00), BigDecimal.valueOf(300.00)),
//                BigDecimal.valueOf(1100.25));
//        // create new guest
//        reservation.setGuest();
//        Reservation actual = repository.add(reservation);
//        assertEquals(1, actual.getId());
//        List<Reservation> all = repository.findAll();
//        assertEquals(1, all.size());
//    }

    @Test
    void shouldFindAllReservations() {
    }

    @Test
    void shouldFindReservationsById() {
    }

    @Test
    void shouldUpdateReservation() {
    }
}