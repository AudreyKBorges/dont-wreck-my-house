package learn.dontwreckmyhouse.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class HostFileRepository {
    private final String filePath;
    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";

    public HostFileRepository(@Value("./data/hosts.csv")String filePath) {
        this.filePath = filePath;
    }
}
