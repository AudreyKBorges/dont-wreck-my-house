package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;

import java.util.List;

public interface GuestRepository {

    List<Guest> findAll() throws DataException;

    Guest findById(int id) throws DataException;
}
