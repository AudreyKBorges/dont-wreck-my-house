package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryTestDouble implements GuestRepository {

    @Override
    public List<Guest> findAll() throws DataException {
        ArrayList<Guest> all = new ArrayList<>();
        all.add(new Guest());
        return all;
    }

    @Override
    public Guest findById(String id) throws DataException {
        for(Guest guest : findAll()){
            if(guest.getId().equals(id)){
                return guest;
            }
        }
        return null;
    }
}
