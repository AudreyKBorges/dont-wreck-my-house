package learn.dontwreckmyhouse.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class GuestFileRepository {
    private final String filePath;
    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";

    public GuestFileRepository(@Value("./data/guests.csv") String filePath) {
        this.filePath = filePath;
    }
}
