package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepositoryTestDouble;
import learn.dontwreckmyhouse.data.HostRepositoryTestDouble;
import learn.dontwreckmyhouse.data.ReservationRepositoryTestDouble;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    GuestService service = new GuestService(
            new GuestRepositoryTestDouble(),
            new ReservationRepositoryTestDouble(),
            new HostRepositoryTestDouble());

    @Test
    void shouldFindGuestByEmail() throws DataException {
        Guest actual = service.findByEmail("slomas0@mediafire.com");

        assertNotNull(actual);
        assertEquals(1, actual.getId());
        assertEquals("Sullivan", actual.getFirstName());
        assertEquals("Lomas", actual.getLastName());
        assertEquals("slomas0@mediafire.com", actual.getEmail());
        assertEquals("(702) 7768761", actual.getPhone());
        assertEquals("NV", actual.getState());
    }

    @Test
    void shouldNotFindByNullEmail() throws DataException {
        Guest guest = new Guest(1, "Sullivan", "Lomas", null, "(702) 7768761", "NV" );
        Guest result = service.findByEmail(null);
        assertNull(result);
    }

    @Test
    void shouldNotFindByBlankEmail() throws DataException {
        Guest guest = new Guest(1, "Sullivan", "Lomas", "", "(702) 7768761", "NV" );
        Guest result = service.findByEmail("");
        assertNull(result);
    }
}