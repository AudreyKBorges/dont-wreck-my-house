package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.domain.GuestService;
import learn.dontwreckmyhouse.domain.HostService;
import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.domain.Result;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

@Component
public class Controller {
    private final View view;
    private final ReservationService reservationService;
    private final HostService hostService;
    private final GuestService guestService;


    public Controller(View view, ReservationService reservationService, HostService hostService, GuestService guestService) {
        this.view = view;
        this.reservationService = reservationService;
        this.hostService = hostService;
        this.guestService = guestService;
    }

    public void run() throws DataException {
        view.displayHeader("Welcome to Don't Wreck My House");
        runMenu();
        view.displayHeader("Goodbye.");
    }

    private void runMenu() throws DataException {
        MenuOption option;
        do {
            option = view.displayMenuAndSelect();
            switch (option) {
                case EXIT:
                    view.displayHeader("Goodbye!");
                    break;
                case VIEW_RESERVATIONS:
                    viewByHost();
                    break;
                case MAKE_RESERVATION:
                    makeReservation();
                    break;
                case EDIT_RESERVATION:
                    editReservation();
                    break;
                case CANCEL_RESERVATION:
                    cancelReservation();
                    break;
            }
        } while (option != MenuOption.EXIT);
    }

    // READ
    private void viewByHost() throws DataException {
        view.displayHeader(MenuOption.VIEW_RESERVATIONS.getTitle());
        // prompt user for email
        String hostEmail = view.hostEmailPrompt();
        Host host = hostService.findByEmail(hostEmail);
        if(host == null) {
            view.displayResult(false, "Please choose a valid host.");
        } else {
            List<Reservation> reservations = reservationService.findByHost(host);
            view.displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));
            view.displayReservations(reservations);
            view.enterToContinue();
        }
    }

    private void makeReservation() throws DataException {
        view.displayHeader(MenuOption.MAKE_RESERVATION.getTitle());
        String guestEmail = view.guestEmailPrompt();
        guestService.findByEmail(guestEmail);
        String hostEmail = view.hostEmailPrompt();
        Host host = hostService.findByEmail(hostEmail);
        if(host == null) {
            view.displayResult(false, "Please choose a valid host.");
        } else {
            List<Reservation> reservations = reservationService.findByHost(host);
            view.displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));
            view.displayReservations(reservations);
        }

        DateTimeFormatter df = new DateTimeFormatterBuilder().parseCaseInsensitive()
                .append(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toFormatter();
        // prompt user for start date
        String userStartDate = view.userStartDate();
        LocalDate date1 = LocalDate.parse(userStartDate, df);
        // prompt user for end date
        String userEndDate = view.userEndDate();
        LocalDate date2 = LocalDate.parse(userEndDate, df);
        Reservation reservation = new Reservation();

        reservationService.validate(reservation);
        // show summary(header) with dates, total
        view.displayHeader("Summary");
        view.displayText(String.format("Start (MM/dd/yyyy): %s, ", date1));
        view.displayText(String.format("End (MM/dd/yyyy): %s, ", date2));
        view.displayText(String.format("Total: %s", reservation.getCalculateTotal(date1, date2)));
        // Ask user Is this okay? [y/n]:
        view.userPrompt();
        // Display message as a header, ie: Success/Error

        Result<Reservation> result = reservationService.add(reservation);
        if (!result.isSuccess()) {
            view.displayResult(false, result.getMessages());
        } else {
            String successMessage = String.format("Reservation %s created.", result.getPayload().getId());
            view.displayResult(true, successMessage);
        }
    }

    private void editReservation() {
        view.displayHeader(MenuOption.EDIT_RESERVATION.getTitle());
        // find a reservation
        // start and end date can be edited
        // recalculate the total & display a summary
        // ask user to confirm
    }

    private void cancelReservation() throws DataException {
        view.displayHeader(MenuOption.CANCEL_RESERVATION.getTitle());
        // find a reservation
        // display a message
    }

}
