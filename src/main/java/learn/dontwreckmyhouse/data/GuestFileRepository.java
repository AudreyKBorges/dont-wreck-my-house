package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestFileRepository implements GuestRepository {
    private final String filePath;
    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";

    public GuestFileRepository(@Value("./data/guests.csv") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Guest> findAll() {
        List<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {

        }
        return result;
    }

    @Override
    public Guest findById(int id) {
        List<Guest> all = findAll();
        for (Guest guest : all) {
            if (guest.getId() == id) {
                return guest;
            }
        }
        return null;
    }

    private void writeToFile(List<Guest> guests) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {

            writer.println(HEADER);

            for (Guest g : guests) {
                String line = serialize(g);
                writer.println(line);
            }
        } catch (IOException exception) {
            throw new DataException("Could not write to file path" + filePath);
        }
    }

    // CREATE

    public Guest add(Guest guest) throws DataException {
        List<Guest> all = findAll();
        int id = getNextId(all);
        guest.setId(id);
        all.add(guest);
        writeToFile(all);
        return guest;
    }

    // UPDATE
    public boolean update(Guest guest) throws DataException {
        List<Guest> all = findAll();
        for(int i = 0; i < all.size(); i++){
            if(all.get(i).getId() == guest.getId()){
                all.set(i, guest);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }

    private int getNextId(List<Guest> guests){
        int maxId = 0;
        for(Guest guest : guests) {
            if(maxId < guest.getId()){
                maxId = guest.getId();
            }
        }
        return maxId + 1;
    }

    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setId(Integer.parseInt(fields[0]));
        result.setFirstName(fields[1]);
        result.setLastName(fields[2]);
        result.setEmail(fields[3]);
        result.setPhone(fields[4]);
        result.setState(fields[5]);

        return result;
    }

    private String serialize(Guest guest) {
        return String.format("%s,%s,%s,%s,%s,%s",
                guest.getId(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEmail(),
                guest.getPhone(),
                guest.getState());
    }
}
