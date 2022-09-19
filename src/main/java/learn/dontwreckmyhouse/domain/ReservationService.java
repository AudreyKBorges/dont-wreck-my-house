package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.data.ReservationRepository;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
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

    private static boolean isStandardRate(LocalDate weekDay) {
        DayOfWeek day = DayOfWeek.of(weekDay.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.MONDAY || day == DayOfWeek.TUESDAY ||
                day == DayOfWeek.WEDNESDAY || day == DayOfWeek.THURSDAY || day == DayOfWeek.FRIDAY;
    }
    public BigDecimal calculateTotal(Reservation reservation) {

        int days = (int) ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        BigDecimal standardRate = reservation.getHost().getWeekendRate();
        BigDecimal weekendRate = reservation.getHost().getStandardRate();

        if(isStandardRate(LocalDate.ofEpochDay(days))) {
            standardRate = reservation.getHost().getStandardRate().multiply(BigDecimal.valueOf(days));
        }
        if(!isStandardRate(LocalDate.ofEpochDay(days))) {
            weekendRate = reservation.getHost().getWeekendRate().multiply(BigDecimal.valueOf(days));
        }

        BigDecimal total = standardRate.add(weekendRate);
        reservation.setTotal(total);

        return total;
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

        if(result.isSuccess()){
            entry = reservationRepository.add(entry);
            result.setPayload(entry);
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
        }
        return result;
    }

    public Reservation findById(int reservationId, Host host) {
        return reservationRepository.findById(reservationId, host);
    }

    // UPDATE
    public Result<Reservation> updateReservation(Reservation entry) throws DataException {
        Result<Reservation> result = validate(entry);

        if(!result.isSuccess()) {
           return result;
        }

        boolean updated = reservationRepository.updateReservation(entry);

        if(!updated){
            result.addMessage(String.format("Reservation entry with id %s does not exist", entry.getId()));
        }

        result.setPayload(entry);

        return result;
    }

    // DELETE
    public Result<Reservation> deleteReservation(Reservation entry) throws DataException {
        Result<Reservation> result = new Result<>();
        if(entry.getStartDate().isBefore(LocalDate.now())) {
            result.addMessage("Cannot cancel a reservation that is in the past");
            return result;
        }
        if(!reservationRepository.deleteReservation(entry)){
            result.addMessage(String.format("Reservation entry with id %s does not exist.", entry.getId()));
        }
        result.setPayload(entry);

        return result;
    }

    public Result validate(Reservation entry) throws DataException {
        Result result = new Result();
        List<Reservation> entries = findByHost(entry.getHost());
        if(entry == null){
            result.addMessage("Reservation entry cannot be null.");
            return result;
        }
        if(entry.getHost() == null) {
            result.addMessage("Host email cannot be null.");
            return result;
        }
        if(entry.getGuest() == null) {
            result.addMessage("Guest email cannot be null.");
            return result;
        }
        if(entry.getStartDate() == null){
            result.addMessage("Start date is required.");
            return result;
        }
        if(entry.getEndDate() == null) {
            result.addMessage("End date is required.");
            return result;
        }
        if(entry.getStartDate().isBefore(LocalDate.now())) {
            result.addMessage("Cannot cancel a reservation that is in the past");
            return result;
        }
        for(Reservation reservation : entries) {
            if(entry.getStartDate().equals(reservation.getStartDate()) && entry.getEndDate().equals(reservation.getEndDate())) {
                result.addMessage("Reservation cannot overlap existing reservation dates");
                return result;
            }
            if(entry.getStartDate().isBefore(reservation.getStartDate()) && entry.getEndDate().isAfter(reservation.getStartDate())) {
                result.addMessage("Reservation cannot overlap existing reservation dates");
                return result;
            }
            if(entry.getStartDate().isBefore(reservation.getEndDate()) && entry.getEndDate().isAfter(reservation.getEndDate())) {
                result.addMessage("Reservation cannot overlap existing reservation dates");
                return result;
            }
            if(entry.getStartDate().isAfter(reservation.getStartDate()) && entry.getEndDate().isBefore(reservation.getEndDate())) {
                result.addMessage("Reservation cannot overlap existing reservation dates");
                return result;
            }
            if(entry.getStartDate().isAfter(entry.getEndDate())) {
                result.addMessage("Start date cannot be after end date");
                return result;
            }
        }
        return result;
    }
}