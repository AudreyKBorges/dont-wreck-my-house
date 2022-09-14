package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class Controller {
    private final View view;
    private final ReservationService service;

    public Controller(View view, ReservationService service) {
        this.view = view;
        this.service = service;
    }

    public void run() {
        view.displayHeader("Welcome to Don't Wreck My House");
        run();
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
                    viewReservations();
                    break;
                case MAKE_RESERVATION:
                    break;
                case EDIT_RESERVATION:
                    break;
                case CANCEL_RESERVATION:
                    break;
            }
        } while (option != MenuOption.EXIT);
    }

    // READ
    private void viewReservations() throws DataException {
        view.displayHeader(MenuOption.VIEW_RESERVATIONS.getTitle());
        String hostEmail = view.emailPrompt();
        List<Reservation> reservations = service.findByHost(hostEmail);
        view.displayHeader();
        view.displayReservations(reservations);
    }


}
