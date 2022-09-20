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
            DateTimeFormatter df = new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .append(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toFormatter();
            // prompt user for start date
            String userStartDate = view.userStartDate();
            LocalDate date1 = LocalDate.parse(userStartDate, df);
            // prompt user for end date
            String userEndDate = view.userEndDate();
            LocalDate date2 = LocalDate.parse(userEndDate, df);
            Reservation newReservation = new Reservation();
            newReservation.setHost(host);
            newReservation.setGuest(guest);
            newReservation.setStartDate(date1);
            newReservation.setEndDate(date2);
            newReservation.setTotal(reservationService.calculateTotal(newReservation));

            Result result = new Result();

            reservationService.validate(newReservation, result);
            // show summary(header) with dates, total
            view.displayHeader("Summary");
            view.displayText(String.format("Start (MM/dd/yyyy): %s ", date1));
            view.displayText(String.format("End (MM/dd/yyyy): %s ", date2));
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
        String hostEmail = view.hostEmailPrompt();
        Host host = hostService.findByEmail(hostEmail);
        if(host == null) {
            view.displayResult(false, "Please choose a valid host.");
            return;
        } else {
            String guestEmail = view.guestEmailPrompt();
            Guest guest = guestService.findByEmail(guestEmail);
            if(guest == null) {
                view.displayResult(false, "Guest cannot be null.");
                return;
            }
            List<Reservation> existingReservations = reservationService.findByHost(host);
            view.displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));
            view.displayReservations(existingReservations);

            Reservation updateReservation = reservationService.findById(guest.getId(), host);
            Reservation newReservation = view.editReservation(updateReservation, host, guest);
            reservationService.calculateTotal(newReservation);
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
        String hostEmail = view.hostEmailPrompt();
        Host host = hostService.findByEmail(hostEmail);

        if(guest == null) {
            view.displayResult(false, "Please enter a guest email address.");
        }
        if(host == null) {
            view.displayResult(false, "Please choose a valid host.");
        }

        view.displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));

        List<Reservation> existingReservations = reservationService.findByHost(host);
        Reservation reservation = reservationService.findById(1, host);

        Result <Reservation> result = reservationService.deleteReservation(reservation, existingReservations);
        if (!result.isSuccess()) {
            view.displayResult(false, result.getMessages());
        } else {
            String successMessage = String.format("Reservation %s deleted.", result.getPayload().getId());
            view.displayResult(true, successMessage);
        }
    }
}
