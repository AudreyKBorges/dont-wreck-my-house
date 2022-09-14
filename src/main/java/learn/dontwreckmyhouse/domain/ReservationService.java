package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.data.ReservationRepository;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final HostRepository hostRepository;

    public ReservationService(ReservationRepository reservationRepository, GuestRepository guestRepository, HostRepository hostRepository) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
    }

    public Result add(Reservation entry) throws DataException {
        Result result = validate(entry);
        if(!result.isSuccess()) {
            return result;
        }
        for(Reservation r : findByHost(entry.getHost())) {
            if(Objects.equals(entry.getId(), r.getId())) {
                if(Objects.equals(entry.getGuest(), r.getGuest())) {
                    if(Objects.equals(entry.getHost(), r.getHost())) {
                        result.addMessage("Cannot create duplicate entry");
                    }
                }
            }
        }

        if(entry.getId() > 0){
            result.addMessage("Cannot create existing entry");
        }

        if(result.isSuccess()){
            entry = reservationRepository.add(entry);
            result.setReservation(entry);
        }
        return result;
    }

    // READ
    public List<Reservation> findByHost(Host host) throws DataException {
        Map<Integer, Guest> guestMap = guestRepository.findAll().stream()
                .collect(Collectors.toMap(guest -> guest.getId(), guest -> guest));
        List<Reservation> result = reservationRepository.findByHost(host);
        for(Reservation reservation : result) {
            reservation.setGuest(guestMap.get(reservation.getGuest().getId()));
            return result;
        }
        return result;
    }

    // UPDATE
    public Result updateReservation(Reservation entry) throws DataException {
        Result result = validate(entry);
        if(!result.isSuccess()){
            return result;
        }
        boolean updated = reservationRepository.updateReservation(entry);

        if(!updated){
            result.addMessage(String.format("Reservation entry with id %s does not exist", entry.getId()));
        }

        return result;
    }

    // DELETE
    public Result deleteReservation(Reservation reservation) throws DataException {
        Result result = new Result();
        if(!reservationRepository.deleteReservation(reservation)){
            result.addMessage(String.format("Reservation entry with id %s does not exist.", reservation));
        }
        return result;
    }

    public Result validate(Reservation entry){
        Result result = new Result();
        ArrayList<Reservation> entries = new ArrayList<>();
        // Guest, host, and start and end dates are required.
        if(entry == null){
            result.addMessage("Reservation entry cannot be null.");
            return result;
        }
        if(entry.getHost() == null) {
            result.addMessage("Host cannot be null.");
            return result;
        }
        if(entry.getGuest() == null) {
            result.addMessage("Guest cannot be null.");
            return result;
        }
        if(entry.getStartDate() == null){
            result.addMessage("Start date is required.");
            return result;
        }
        if(entry.getEndDate() == null){
            result.addMessage("End date is required.");
            return result;
        }
        if(entry.getEndDate().isBefore(entry.getStartDate())){
            result.addMessage("End date cannot come before start date.");
            return result;
        }

        // The reservation may never overlap existing reservation dates.

        // Cannot cancel a reservation that's in the past.

        // The start date must be in the future.

        return result;
    }
}