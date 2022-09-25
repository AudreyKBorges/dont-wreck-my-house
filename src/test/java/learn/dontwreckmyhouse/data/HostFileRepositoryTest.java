package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/hosts-seed.csv";
    static final String TEST_FILE_PATH = "./data/hosts-test.csv";

    private final HostFileRepository repository = new HostFileRepository(TEST_FILE_PATH);


    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAllHosts() throws DataException {
        List<Host> actual = repository.findAll();
        assertEquals(2, actual.size());

        Host host = actual.get(0);
        assertEquals("3edda6bc-ab95-49a8-8962-d50b53f84b15", host.getId());
        assertEquals("Yearnes", host.getLastName());
        assertEquals("eyearnes0@sfgate.com", host.getEmail());
        assertEquals("(806) 1783815", host.getPhoneNumber());
        assertEquals("3 Nova Trail", host.getAddress());
        assertEquals("Amarillo", host.getCity());
        assertEquals("TX", host.getState());
        assertEquals("79182", host.getPostalCode());
        assertEquals(BigDecimal.valueOf(Double.parseDouble(String.valueOf(340))), host.getStandardRate());
        assertEquals(BigDecimal.valueOf(Double.parseDouble(String.valueOf(425))), host.getWeekendRate());

        Host host2 = actual.get(1);
        assertEquals("a0d911e7-4fde-4e4a-bdb7-f047f15615e8", host2.getId());
        assertEquals("Rhodes", host2.getLastName());
        assertEquals("krhodes1@posterous.com", host2.getEmail());
        assertEquals("(478) 7475991", host2.getPhoneNumber());
        assertEquals("7262 Morning Avenue", host2.getAddress());
        assertEquals("Macon", host2.getCity());
        assertEquals("GA", host2.getState());
        assertEquals("31296", host2.getPostalCode());
        assertEquals(BigDecimal.valueOf(Double.parseDouble(String.valueOf(295))), host2.getStandardRate());
        assertEquals(BigDecimal.valueOf(Double.parseDouble(String.valueOf(368.75))), host2.getWeekendRate());

    }

    @Test
    void shouldFindAllHostsById() throws DataException {
        Host actual = repository.findById("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertNotNull(actual);
        assertEquals("Yearnes", actual.getLastName());
        assertEquals("eyearnes0@sfgate.com", actual.getEmail());
        assertEquals("(806) 1783815", actual.getPhoneNumber());
        assertEquals("3 Nova Trail", actual.getAddress());
        assertEquals("Amarillo", actual.getCity());
        assertEquals("TX", actual.getState());
        assertEquals("79182", actual.getPostalCode());
        assertEquals(BigDecimal.valueOf(Double.parseDouble(String.valueOf(340))), actual.getStandardRate());
        assertEquals(BigDecimal.valueOf(Double.parseDouble(String.valueOf(425))), actual.getWeekendRate());
    }
}