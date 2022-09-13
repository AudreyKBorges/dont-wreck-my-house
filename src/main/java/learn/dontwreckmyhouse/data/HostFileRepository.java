package learn.dontwreckmyhouse.data;

import org.springframework.stereotype.Repository;

@Repository
public class HostFileRepository {
    private final String filePath;

    public HostFileRepository(String filePath) {
        this.filePath = filePath;
    }
}
