package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    // CREATE
    Reservation add(Reservation reservation) throws DataException;

    // READ
    List<Reservation> findAll() throws DataException;

    // UPDATE
    boolean update(Reservation reservation) throws DataException;


}
