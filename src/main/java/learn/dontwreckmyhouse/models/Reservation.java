package learn.dontwreckmyhouse.models;

import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

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
    public BigDecimal getCalculateTotal(String startDate, String endDate) {
//        logic-wise, I would suggest creating a loop that add a day to the starting day
//        until starting day is the ending day, then for each day, check what day of the week it is.
//        if its a weekday, add the standard rate to the total, and if it is weekend day, add the
//        weekend rate to the total.
        LocalDate startingDay = LocalDate.from(getStartDate());
        LocalDate endingDay = LocalDate.from(getEndDate());
        BigDecimal standardRate = new BigDecimal(String.valueOf(host.getStandardRate())).
                setScale(4, RoundingMode.HALF_UP);
        BigDecimal weekendRate = new BigDecimal(String.valueOf(host.getWeekendRate())).
                setScale(4, RoundingMode.HALF_UP);

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