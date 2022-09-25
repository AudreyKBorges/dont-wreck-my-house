package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepositoryTestDouble;
import learn.dontwreckmyhouse.data.HostRepositoryTestDouble;
import learn.dontwreckmyhouse.data.ReservationRepositoryTestDouble;
import learn.dontwreckmyhouse.models.Host;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    HostService service = new HostService(
            new HostRepositoryTestDouble(),
            new ReservationRepositoryTestDouble(),
            new GuestRepositoryTestDouble());

    @Test
    void shouldFindReservationByEmail() throws DataException {
        Host actual = service.findByEmail("eyearnes0@sfgate.com");

        assertNotNull(actual);
        assertEquals("3edda6bc-ab95-49a8-8962-d50b53f84b15", actual.getId());
        assertEquals("Yearnes", actual.getLastName());
        assertEquals("(806) 1783815", actual.getPhoneNumber());
        assertEquals("3 Nova Trail", actual.getAddress());
        assertEquals("Amarillo", actual.getCity());
        assertEquals("TX", actual.getState());
        assertEquals("79182", actual.getPostalCode());
        assertEquals(BigDecimal.valueOf(Double.valueOf(340)), actual.getStandardRate());
        assertEquals(BigDecimal.valueOf(Double.valueOf(425)), actual.getWeekendRate());
    }

    @Test
    void shouldNotFindByNullEmail() throws DataException {
        Host host = new Host("3edda6bc-ab95-49a8-8962-d50b53f84b15","Yearnes",null,"(806) 1783815","3 Nova Trail","Amarillo","TX","79182", BigDecimal.valueOf(Double.parseDouble(String.valueOf(340))),BigDecimal.valueOf(Double.parseDouble(String.valueOf(425))));
        Host result = service.findByEmail(null);
        assertNull(result);
    }

    @Test
    void shouldNotFindByBlankEmail() throws DataException {
        Host host = new Host("3edda6bc-ab95-49a8-8962-d50b53f84b15","Yearnes","","(806) 1783815","3 Nova Trail","Amarillo","TX","79182", BigDecimal.valueOf(Double.parseDouble(String.valueOf(340))),BigDecimal.valueOf(Double.parseDouble(String.valueOf(425))));
        Host result = service.findByEmail("");
        assertNull(result);
    }
}