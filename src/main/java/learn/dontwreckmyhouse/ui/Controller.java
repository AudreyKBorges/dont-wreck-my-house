package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.domain.GuestService;
import learn.dontwreckmyhouse.domain.HostService;
import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Component;

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
                    break;
                case CANCEL_RESERVATION:
                    break;
            }
        } while (option != MenuOption.EXIT);
    }

    // READ
    private void viewByHost() throws DataException {
        view.displayHeader(MenuOption.VIEW_RESERVATIONS.getTitle());
        // prompt user for email
        String hostEmail = view.emailPrompt();
        Host host = hostService.findByEmail(hostEmail);
        if(host == null) {
            view.displayResult(false, "Please pick a valid host.");
        } else {
            List<Reservation> reservations = reservationService.findByHost(host);
            view.displayHeader(String.format("%s %s %s", host.getLastName(), host.getCity(), host.getState()));
            view.displayReservations(reservations);
            view.enterToContinue();
        }
    }

    private void makeReservation() {
        view.displayHeader(MenuOption.MAKE_RESERVATION.getTitle());
    }

}
