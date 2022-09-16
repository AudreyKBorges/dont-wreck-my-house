package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
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

    private String getDirectory(Host host) {
        return Paths.get(directory, host.getId() + ".csv").toString();
    }

    // CREATE
    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> all = findByHost(reservation.getHost());
        int id = getNextId(all);
        reservation.setId(id);
        all.add(reservation);
        writeToFile(all, reservation.getHost());
        return reservation;
    }

    // READ
    @Override
    public List<Reservation> findByHost(Host host) {
        ArrayList<Reservation> result = new ArrayList<>();
        if(host == null) {
            return result;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(getDirectory(host)))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields, host));
                } // add error message
            }
        } catch (IOException ex) {

        }
        return result;
    }

    // UPDATE
    @Override
    public boolean updateReservation(Reservation reservation)  throws DataException {
        List<Reservation> all = findByHost(reservation.getHost());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == reservation.getId()) {
                all.set(i, reservation);
                writeToFile(all, reservation.getHost());
                return true;
            }
        }
        return false;
    }

    // DELETE
    @Override
    public boolean deleteReservation(Reservation reservation) throws DataException {
        List<Reservation> all = findByHost(reservation.getHost());
        for(int i = 0; i < all.size(); i++){
            if(all.get(i).getId() == reservation.getId()) {
                all.remove(i);
                writeToFile(all, reservation.getHost());
                return true;
            }
        }
        return false;
    }

    public Reservation findById(int id, Host host) throws DataException {
        List<Reservation> all = findByHost(host);
        for(Reservation reservation : all){
            if(reservation.getId() == id) {
                return  reservation;
            }
        }
        return null;
    }

    private int getNextId(List<Reservation> reservations){
        int maxId = 0;
        for(Reservation reservation : reservations) {
            if(maxId < reservation.getId()){
                maxId = reservation.getId();
            }
        }
        return maxId + 1;
    }

    private void writeToFile(List<Reservation> reservations, Host host) throws DataException {
        try(PrintWriter writer = new PrintWriter(getDirectory(host))) {
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
        guest.setId(Integer.parseInt(fields[3]));
        result.setGuest(guest);
        return result;
    }
}
