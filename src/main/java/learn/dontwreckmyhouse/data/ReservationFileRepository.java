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

    public String getDirectory() {
        return directory;
    }

    // CREATE
    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> all = findByHost(reservation.getHost());
        int nextId = getNextId(all);
        reservation.setId(nextId);
        all.add(reservation);
        writeToFile(all);
        return reservation;
    }

    // READ
    @Override
    public List<Reservation> findByHost(Host host) throws DataException {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getDirectory()))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 7) {
                    result.add(deserialize(fields, host));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    // UPDATE
    @Override
    public boolean updateReservation(Reservation reservation)  throws DataException {
        List<Reservation> entries = findByHost(reservation.getHost());
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getId() == reservation.getId()) {
                entries.set(i, reservation);
                writeToFile(entries);
                return true;
            }
        }
        return false;
    }

    // DELETE
    @Override
    public boolean deleteReservation(Reservation reservation) throws DataException {
        List<Reservation> entries = findByHost(reservation.getHost());
        for(int i = 0; i < entries.size(); i++){
            if(entries.get(i).getId() == reservation){
                entries.remove(i);
                writeToFile(entries);
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
}
