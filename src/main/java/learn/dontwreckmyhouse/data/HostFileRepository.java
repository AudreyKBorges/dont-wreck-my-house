package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class HostFileRepository implements HostRepository {
    private final String filePath;
    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";

    public HostFileRepository(@Value("./data/hosts.csv")String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Host> findAll() throws DataException {
        ArrayList<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {

        }
        return result;
    }

    @Override
    public Host findById(String id) throws DataException {
        List<Host> all = findAll();
        for (Host host : all) {
            if (Objects.equals(host.getId(), id)) {
                return host;
            }
        }
        return null;
    }

    private void writeAll(List<Host> hosts) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {

            writer.println(HEADER);

            for (Host h : hosts) {
                String line = serialize(h);
                writer.println(line);
            }
        } catch (IOException exception) {
            throw new DataException("Could not write to file path" + filePath);
        }
    }

    private Host deserialize(String[] fields) {
        Host result = new Host();

        result.setId(fields[0]);
        result.setLastName(fields[1]);
        result.setEmail(fields[2]);
        result.setPhoneNumber(fields[3]);
        result.setAddress(fields[4]);
        result.setCity(fields[5]);
        result.setState(fields[6]);
        result.setPostalCode(fields[7]);
        result.setStandardRate(BigDecimal.valueOf(Double.parseDouble(fields[8])));
        result.setWeekendRate(BigDecimal.valueOf(Double.parseDouble(fields[9])));

        return result;
    }

    private String serialize(Host host) {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                host.getId(),
                host.getLastName(),
                host.getEmail(),
                host.getAddress(),
                host.getCity(),
                host.getState(),
                host.getPostalCode());

    }
}
