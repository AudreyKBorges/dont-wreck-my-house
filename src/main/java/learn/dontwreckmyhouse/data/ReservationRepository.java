package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByHost(Host host) throws DataException;

    Reservation findById(int id, Host host);
    Reservation add(Reservation reservation)throws DataException;
    boolean updateReservation(Reservation reservation) throws DataException;
    boolean deleteReservation(Reservation reservation) throws DataException;
}
