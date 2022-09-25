package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {
    static final String SEED_FILE_PATH = "./data/guests-seed.csv";
    static final String TEST_FILE_PATH = "./data/guests-test.csv";

    private final GuestFileRepository repository = new GuestFileRepository(TEST_FILE_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAllGuests() {
        List<Guest> actual = repository.findAll();
        assertEquals(2, actual.size());

        Guest guest = actual.get(0);
        assertEquals(1, guest.getId());
        assertEquals("Sullivan", guest.getFirstName());
        assertEquals("Lomas", guest.getLastName());
        assertEquals("slomas0@mediafire.com", guest.getEmail());
        assertEquals("(702) 7768761", guest.getPhone());
        assertEquals("NV", guest.getState());

        Guest guest2 = actual.get(1);
        assertEquals(2, guest2.getId());
        assertEquals("Olympie", guest2.getFirstName());
        assertEquals("Gecks", guest2.getLastName());
        assertEquals("ogecks1@dagondesign.com", guest2.getEmail());
        assertEquals("(202) 2528316", guest2.getPhone());
        assertEquals("DC", guest2.getState());
    }

    @Test
    void shouldFindGuestById() {
        Guest actual = repository.findById(1);

        assertNotNull(actual);
        assertEquals(1, actual.getId());
        assertEquals("Sullivan", actual.getFirstName());
        assertEquals("Lomas", actual.getLastName());
        assertEquals("slomas0@mediafire.com", actual.getEmail());
        assertEquals("(702) 7768761", actual.getPhone());
        assertEquals("NV", actual.getState());
    }

    @Test
    void shouldAddGuest() {
        Guest guest = new Guest();
        guest.setId(3);
        guest.setFirstName("Cindy");
        guest.setLastName("Summers");
        guest.setEmail("cindysummers@gmail.com");
        guest.setPhone("(802) 4442222");
        guest.setState("VT");

        assertNotNull(guest);

        assertEquals(3, guest.getId());
        assertEquals("Cindy", guest.getFirstName());
        assertEquals("Summers", guest.getLastName());
        assertEquals("cindysummers@gmail.com", guest.getEmail());
        assertEquals("(802) 4442222", guest.getPhone());
        assertEquals("VT", guest.getState());
    }
}