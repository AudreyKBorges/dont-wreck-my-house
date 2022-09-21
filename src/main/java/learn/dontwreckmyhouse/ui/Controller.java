package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.domain.GuestService;
import learn.dontwreckmyhouse.domain.HostService;
import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.domain.Result;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
        Guest guest = guestService.findByEmail(guestEmail);
        if(guest == null) {
            view.displayResult(false, "Guest cannot be null.");
            return;
        }
        String hostEmail = view.hostEmailPrompt();
        Host host = hostService.findByEmail(hostEmail);
        if(host == null) {
            view.displayResult(false, "Please choose a valid host.");
        } else {
            List<Reservation> existingReservations = reservationService.findByHost(host);
            view.displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));
            view.displayReservations(existingReservations);

            // prompt user for start date
            LocalDate userStartDate = view.userStartDate();
            // prompt user for end date
            LocalDate userEndDate = view.userEndDate();
            Reservation newReservation = new Reservation();
            newReservation.setHost(host);
            newReservation.setGuest(guest);
            newReservation.setStartDate(userStartDate);
            newReservation.setEndDate(userEndDate);
            newReservation.setTotal(reservationService.calculateTotal(newReservation));

            Result result = new Result();

            reservationService.validate(newReservation, result);
            // show summary(header) with dates, total
            view.displayHeader("Summary");
            view.displayText(String.format("Start (MM/dd/yyyy): %s ", userStartDate));
            view.displayText(String.format("End (MM/dd/yyyy): %s ", userEndDate));
            view.displayText(String.format("Total: %s", newReservation.getCalculateTotal()));
            // Ask user Is this okay? [y/n]:
            view.userPrompt();

            // Display message as a header, ie: Success/Error
            result = reservationService.add(newReservation);
            if (!result.isSuccess()) {
                view.displayResult(false, result.getMessages());
            } else {
                String successMessage = String.format("Reservation %s created.", result.getPayload());
                view.displayResult(true, successMessage);
            }
        }
    }

    private void editReservation() throws DataException {
        view.displayHeader(MenuOption.EDIT_RESERVATION.getTitle());
        String guestEmail = view.guestEmailPrompt();
        Guest guest = guestService.findByEmail(guestEmail);
        if(guest == null) {
            view.displayResult(false, "Guest cannot be null.");
            return;
        }
        String hostEmail = view.hostEmailPrompt();
        Host host = hostService.findByEmail(hostEmail);
        if(host == null) {
            view.displayResult(false, "Please choose a valid host.");
        } else {
            List<Reservation> existingReservations = reservationService.findByHost(host);
            view.displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));
            view.displayReservations(existingReservations);

            String id = String.valueOf(view.promptForId());
            Reservation updateReservation = reservationService.findById(Integer.parseInt(id), host);

            Reservation newReservation = view.editReservation(updateReservation, host, guest);
            view.displayText(String.format("Total: %s", updateReservation.getCalculateTotal()));
            view.userPrompt();

            Result <Reservation> result = reservationService.updateReservation(newReservation, existingReservations);
            if (!result.isSuccess()) {
                view.displayResult(false, result.getMessages());
            } else {
                String successMessage = String.format("Reservation %s updated.", result.getPayload().getId());
                view.displayResult(true, successMessage);
            }
        }
    }

    private void cancelReservation() throws DataException {
        view.displayHeader(MenuOption.CANCEL_RESERVATION.getTitle());
        String guestEmail = view.guestEmailPrompt();
        Guest guest = guestService.findByEmail(guestEmail);
        if(guest == null) {
            view.displayResult(false, "Guest cannot be null.");
            return;
        }
        String hostEmail = view.hostEmailPrompt();
        Host host = hostService.findByEmail(hostEmail);
        if(host == null) {
            view.displayResult(false, "Please choose a valid host.");
        } else {

            List<Reservation> existingReservations = reservationService.findByHost(host);
            view.displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));
            view.displayReservations(existingReservations);

            String id = String.valueOf(view.promptForId());
            Reservation reservation = reservationService.findById(Integer.parseInt(id), host);

            Result<Reservation> result = reservationService.deleteReservation(reservation, existingReservations);
            if (!result.isSuccess()) {
                view.displayResult(false, result.getMessages());
            } else {
                String successMessage = String.format("Reservation %s deleted.", reservation.getId());
                view.displayResult(true, successMessage);
            }
        }
    }
}
