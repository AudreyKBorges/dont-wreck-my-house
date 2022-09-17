package learn.dontwreckmyhouse.models;

import learn.dontwreckmyhouse.data.ReservationFileRepository;
import learn.dontwreckmyhouse.data.ReservationRepository;
import learn.dontwreckmyhouse.domain.ReservationService;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class Reservation {
    // fields
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Guest guest;
    private Host host;

    private BigDecimal calculateTotal;
    private BigDecimal total;

    public Reservation(int id, LocalDate startDate, LocalDate endDate, Guest guest, Host host, BigDecimal calculateTotal, BigDecimal total) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guest = guest;
        this.host = host;
        this.calculateTotal = calculateTotal;
        this.total = total;
    }

    // getValue forage

    public Reservation() {

    }

    public void setCalculateTotal(BigDecimal calculateTotal) {
        this.calculateTotal = calculateTotal;
    }
    public BigDecimal getCalculateTotal(LocalDate startDate, LocalDate endDate) {
        LocalDate startingDay = LocalDate.from(startDate);
        LocalDate endingDay = LocalDate.from(endDate);
//        public final ReservationService reservationService;
//        Host host = reservationService.findByHost(host);
        Host host = new Host();
        BigDecimal standardRate = host.getStandardRate();
        BigDecimal weekendRate = host.getWeekendRate();

        if (total == null) {
            return BigDecimal.ZERO;
        }

        for (; startingDay.compareTo(endingDay) <= 0; startingDay = startingDay.plusDays(1)) {
            if (startingDay.equals(DayOfWeek.MONDAY) || startingDay.equals(DayOfWeek.TUESDAY) ||
                    startingDay.equals(DayOfWeek.WEDNESDAY) || startingDay.equals(DayOfWeek.THURSDAY) ||
                    startingDay.equals(DayOfWeek.FRIDAY)) {
                return standardRate.add(total);
            }
            if (startingDay.equals(DayOfWeek.SATURDAY) || startingDay.equals(DayOfWeek.SUNDAY)) {
                return weekendRate.add(total);
            }
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString(){
        return String.format("ID: %s, %s - %s %s", id, startDate, endDate, guest);
    }
}