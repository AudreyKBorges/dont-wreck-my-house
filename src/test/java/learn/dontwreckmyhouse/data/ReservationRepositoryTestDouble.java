package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryTestDouble implements ReservationRepository {

    public final static Reservation RESERVATION = new Reservation(1,LocalDate.of(2022,10,11),
            LocalDate.of(2022,10,13), new Guest(), new Host(), BigDecimal.valueOf(663), BigDecimal.valueOf(663));

    @Override
    public List<Reservation> findByHost(Host host) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        Reservation reservation = new Reservation();
        reservation.setId(0);
        reservation.setStartDate(LocalDate.of(2022,10,31));
        reservation.setEndDate(LocalDate.of(2022,11,5));
        reservation.setHost(host);

        Guest guest = new Guest();
        guest.setId(1);
        reservation.setTotal(BigDecimal.valueOf(300));
        reservation.setGuest(guest);
        reservations.add(reservation);
        return reservations;
    }

    @Override
    public Reservation findById(int id, Host host) { {
        for(Reservation reservation : findByHost(host)){
            if(reservation.getId() == id){
                return reservation;
            }
        }
        return null;
    }
    }

    @Override
    public Reservation add(Reservation reservation) {
        return reservation;
    }

    @Override
    public boolean updateReservation(Reservation reservation) {
        if(reservation.getId() == 999) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteReservation(Reservation reservation) {
        return true;
    }
}
