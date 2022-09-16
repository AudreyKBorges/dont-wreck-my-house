package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryTestDouble implements GuestRepository {

    public final static Guest GUEST = new Guest(1,"Sullivan","Lomas","slomas0@mediafire.com","(702) 7768761","NV");

    @Override
    public List<Guest> findAll() {
        ArrayList<Guest> all = new ArrayList<>();
        all.add(new Guest(1,"Sullivan","Lomas","slomas0@mediafire.com","(702) 7768761","NV"));
        return all;
    }

    @Override
    public Guest findById(int id) {
        for(Guest guest : findAll()){
            if(guest.getId() == id){
                return guest;
            }
        }
        return null;
    }
}
