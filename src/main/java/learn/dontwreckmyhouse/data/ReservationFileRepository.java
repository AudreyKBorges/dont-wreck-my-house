package learn.dontwreckmyhouse.data;

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
    public List<Reservation> findAll() {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(directory))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 4) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    public Reservation findById(int reservationId) {
        List<Reservation> all = findAll();
        for(Reservation reservation : all){
            if(reservation.getId() == reservationId){
                return  reservation;
            }
        }
        return null;
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

    private String getDirectory(LocalDate date) {
        return Paths.get(directory, date + ".csv").toString();
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

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s",
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuestId(),
                reservation.getTotal());
    }

    private Reservation deserialize(String[] fields) {
        Reservation result = new Reservation();
        result.setId(Integer.parseInt(fields[0]));
        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));
        result.setGuestId(Integer.parseInt(fields[3]));
        result.setTotal(BigDecimal.valueOf(Integer.parseInt(fields[4])));

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
