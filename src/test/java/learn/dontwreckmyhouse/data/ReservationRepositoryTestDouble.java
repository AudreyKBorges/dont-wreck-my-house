package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryTestDouble implements ReservationRepository {
    @Override
    public List<Reservation> findByHost(Host host) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation());
        return reservations;
    }

    @Override
    public Reservation add(Reservation reservation) {
        return reservation;
    }

    @Override
    public boolean updateReservation(Reservation reservation) {
        return true;
    }

    @Override
    public boolean deleteReservation(Reservation reservation) {
        return true;
    }
}
