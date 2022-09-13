package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository {
    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;

    public ReservationFileRepository(@Value("./data/reservations") String directory){

        this.directory = directory;
    }

    // CREATE
    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> all = findAll();
        int nextId = getNextId(all);
        reservation.setId(nextId);
        all.add(reservation);
        writeToFile(all);
        return reservation;
    }

    // READ
    @Override
    public List<Reservation> findAll() throws DataException {
        List<Reservation> result = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(directory))){
            reader.readLine();

            for(String line = reader.readLine(); line != null; line = reader.readLine()){
                Reservation entry = deserialize(line);
                result.add(entry);
            }
        }catch(FileNotFoundException ex){

        }catch(IOException ex){
            throw new DataException("Could not open directory: " + directory, ex);
        }
        return result;
    }

    // UPDATE
    @Override
    public boolean update(Reservation reservation) throws DataException {
        List<Reservation> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == reservation.getId()) {
                all.set(i, reservation);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }

    private void writeToFile(List<Reservation> reservations) throws DataException {
        try(PrintWriter writer = new PrintWriter(directory)) {
            writer.println(HEADER);
            for(Reservation r : reservations) {
                String line = serialize(r);
                writer.println(line);
            }
        } catch (IOException exception) {
            throw new DataException("Could not write to directory" + directory);
        }
    }

    private String serialize(Reservation entry){
        return String.format("%s,%s,%s,%s,%s",
                entry.getId(),
                entry.getStartDate(),
                entry.getEndDate(),
                entry.getGuest().getId(),
                entry.getTotal());
    }

    private Reservation deserialize(String[] fields, Host host){
        Reservation result = new Reservation();

        result.setId(Integer.parseInt(fields[0]));
        result.setHost(host);
        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));
        result.setTotal(BigDecimal.valueOf(Double.parseDouble(fields[4])));

        Guest guest = new Guest();
        result.setGuest(guest);
        guest.setId(Integer.parseInt(fields[3]));
        return result;
    }

    private int getNextId(List<Reservation> reservations){
        int maxId = 0;
        for(Reservation reservation : reservations){
            if(maxId < reservation.getId()){
                maxId = reservation.getId();
            }
        }
        return maxId + 1;
    }
}
