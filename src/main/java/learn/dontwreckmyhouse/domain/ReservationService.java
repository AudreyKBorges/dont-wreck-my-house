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
import java.util.List;
import java.util.Map;
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

        LocalDate day = reservation.getStartDate();

        BigDecimal totalCost = BigDecimal.ZERO;
        do {
            // is today a weekend or standard day?
            totalCost = isStandardRate(day) ?
                    totalCost.add(reservation.getHost().getWeekendRate()) :
                    totalCost.add(reservation.getHost().getStandardRate());
            day = day.plusDays(1);
        } while (day.isBefore(reservation.getEndDate()));
        reservation.setTotal(totalCost);

        return totalCost;
    }

    public Result add(Reservation newReservation) throws DataException {
        Result result = validateDuplicateDates(newReservation);
        if (!result.isSuccess()) {
            return result;
        }
        if (result.isSuccess()) {
            newReservation = reservationRepository.add(newReservation);

            result.setPayload(newReservation);
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
    public Result<Reservation> updateReservation(Reservation newReservation, List<Reservation> existingReservations) throws DataException {
        Result result = validateDuplicateDates(newReservation);

        if(!result.isSuccess()) {
           return result;
        }

        boolean updated = reservationRepository.updateReservation(newReservation);

        if(!updated){
            result.addMessage(String.format("Reservation entry with id %s does not exist", newReservation.getId()));
        }

        result.setPayload(newReservation);

        return result;
    }

    // DELETE
    public Result<Reservation> deleteReservation(Reservation reservation, List<Reservation> existingReservations) throws DataException {
        Result result = validateDuplicateDates(reservation);
        if(!reservationRepository.deleteReservation(reservation)){
            result.addMessage(String.format("Reservation entry with id %s does not exist.", reservation.getId()));
            return result;
        }

        existingReservations = findByHost(reservation.getHost());
        result = new Result();
        for(Reservation r : existingReservations) {
            if(r.getStartDate().isBefore(LocalDate.now())) {
                result.addMessage("Cannot cancel a reservation that is in the past");
                return result;
            }
        }
        return result;
    }

    public Result validate(Reservation newReservation, Result result) throws DataException {
        result = new Result();

        if(newReservation.getHost() == null) {
            result.addMessage("Host email cannot be null.");
            return result;
        }
        if(newReservation.getGuest() == null) {
            result.addMessage("Guest email cannot be null.");
            return result;
        }
        if(newReservation.getStartDate() == null){
            result.addMessage("Start date is required.");
            return result;
        }
        if(newReservation.getEndDate() == null) {
            result.addMessage("End date is required.");
            return result;
        }
        if(newReservation.getStartDate().isBefore(LocalDate.now())) {
            result.addMessage("Cannot cancel a reservation that is in the past");
            return result;
        }
        return result;
    }

    public Result validateDuplicateDates(Reservation newReservation) throws DataException {
        List<Reservation> existingReservations = findByHost(newReservation.getHost());
        Result result = new Result();
        for(Reservation reservation : existingReservations) {
            if(newReservation.getStartDate().equals(reservation.getStartDate()) && newReservation.getEndDate().equals(reservation.getEndDate())) {
                result.addMessage("Reservation cannot overlap existing reservation dates");
                return result;
            }
            if(newReservation.getStartDate().isBefore(reservation.getStartDate()) && newReservation.getEndDate().isAfter(reservation.getStartDate())) {
                result.addMessage("Reservation cannot overlap existing reservation dates");
                return result;
            }
            if(newReservation.getStartDate().isBefore(reservation.getEndDate()) && newReservation.getEndDate().isAfter(reservation.getEndDate())) {
                result.addMessage("Reservation cannot overlap existing reservation dates");
                return result;
            }
            if(newReservation.getStartDate().isAfter(reservation.getStartDate()) && newReservation.getEndDate().isBefore(reservation.getEndDate())) {
                result.addMessage("Reservation cannot overlap existing reservation dates");
                return result;
            }
            if(newReservation.getStartDate().isAfter(newReservation.getEndDate())) {
                result.addMessage("Start date cannot be after end date");
                return result;
            }
        }
        return result;
    }
}