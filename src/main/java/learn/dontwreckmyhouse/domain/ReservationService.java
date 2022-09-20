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
<<<<<<< HEAD
        LocalDate day = reservation.getStartDate();
=======
LocalDate day = reservation.getStartDate();
>>>>>>> a5d28132e8ed0bf1904ec098140dc8679aa7c3b5
        BigDecimal totalCost = BigDecimal.ZERO;
        do{
            // is today a weekend or standard day?
            totalCost = isStandardRate(day) ?
                    totalCost.add(reservation.getHost().getWeekendRate()) :
                    totalCost.add(reservation.getHost().getStandardRate());
            day = day.plusDays(1);
        }while(day.isBefore(reservation.getEndDate()));
<<<<<<< HEAD
        reservation.setTotal(totalCost);

        return totalCost;
=======

        reservation.setTotal(totalCost);
>>>>>>> a5d28132e8ed0bf1904ec098140dc8679aa7c3b5
    }

    public Result add(Reservation newReservation, List<Reservation> existingReservations) throws DataException {
        Result result = validateDuplicateDates(newReservation, existingReservations);
        if(!result.isSuccess()) {
            return result;
        }
        if(result.isSuccess()){
            newReservation = reservationRepository.add(newReservation);

            result.setPayload(newReservation);
        }
        return result;
    }

    public Result validateDuplicateDates(Reservation newReservation, List<Reservation> existingReservations, Result result) throws DataException {
        existingReservations = findByHost(newReservation.getHost());
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
    public Result<Reservation> updateReservation(Reservation newReservation, List<Reservation> existingReservations, Result result) throws DataException {
        result = validate(newReservation, existingReservations, result);

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
    public Result<Reservation> deleteReservation(Reservation newReservation, List<Reservation> existingReservations) throws DataException {
        Result<Reservation> result = new Result<>();
        if(!reservationRepository.deleteReservation(newReservation)){
            result.addMessage(String.format("Reservation entry with id %s does not exist.", newReservation.getId()));
            return result;
        }

        do {
            result.addMessage("Cannot cancel a reservation that is in the past");
            return result;

        } while(newReservation.getStartDate().isBefore(LocalDate.now()));
    }

    public Result validate(Reservation newReservation, List<Reservation> existingReservations, Result result) throws DataException {
        result = new Result();
        existingReservations = findByHost(newReservation.getHost());

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