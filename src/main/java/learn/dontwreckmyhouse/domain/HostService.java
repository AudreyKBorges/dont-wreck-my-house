package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.data.ReservationRepository;
import learn.dontwreckmyhouse.models.Host;
import org.springframework.stereotype.Service;

@Service
public class HostService {
    private final HostRepository repository;
    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;

    public HostService(HostRepository repository, ReservationRepository reservationRepository, GuestRepository guestRepository) {
        this.repository = repository;
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
    }

    public Host findByEmail(String email) throws DataException {
        return repository.findAll().stream()
                .filter(h -> h.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
