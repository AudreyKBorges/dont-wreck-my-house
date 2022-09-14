package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.models.Guest;
import org.springframework.stereotype.Service;

@Service
public class GuestService {
    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public Guest findByEmail(String email) throws DataException {
        return repository.findAll().stream()
                .filter(h -> h.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    private Result validate(Guest guest) {

        Result result = validateNulls(guest);
        result.isSuccess();

        return result;
    }
    
    private Result validateNulls(Guest guest) {
        Result result = new Result();

        if (guest == null) {
            result.addMessage("Nothing to save");
            return result;
        }

        if (guest.getFirstName() == null) {
            result.addMessage("First name required");
        }

        if (guest.getLastName() == null) {
            result.addMessage("Last name required");
        }

        if (guest.getEmail() == null) {
            result.addMessage("State is required");
        }

        if (guest.getPhone() == null) {
            result.addMessage("Phone number is required");
        }

        if (guest.getState() == null) {
            result.addMessage("State is required");
        }

        return result;
    }
}