package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@Component
public class View {
    private final Scanner console = new Scanner(System.in);

    public MenuOption displayMenuAndSelect() {
        MenuOption[] values = MenuOption.values();
        displayHeader("Main Menu");
        for(int i = 0; i < values.length; i++) {
            System.out.printf("%s. %s%n", i, values[i].getTitle());
        }
        int index = readInt("Select [0 - 4]", 0, 4);
        return values[index];
    }

    public void displayReservations(List<Reservation> reservations){
        if(reservations == null) {
            displayText("Host does not exist");
        }
        if(reservations.isEmpty()) {
            displayText("No reservations found for this host");
            return;
        }
        reservations.stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .forEach(System.out::println);
    }

    // Helper methods
    public void displayHeader(String message){
        System.out.println();
        System.out.println(message);
        System.out.println("=".repeat(message.length()));
    }

    public String emailPrompt() {
        return readRequiredString("Host email: ");
    }

    public String userStartDate() {
        return readRequiredString("Start (MM/DD/YYYY): ");
    }

    public String userEndDate() {
        return readRequiredString("End (MM/DD/YYYY): ");
    }

    public void displayResult(boolean success, String message) {
        displayResult(success, List.of(message));
    }

    public void displayResult(boolean success, List<String> messages) {
        displayHeader(success ? "success" : "error");
        for(String message : messages) {
            displayText(message);
        }
    }

    public void displayText(String line){
        System.out.println(line);
    }

    private int readInt(String prompt){
        int result = 0;
        boolean isValid = false;

        do {
            String value = readRequiredString(prompt);
            try {
                result = Integer.parseInt(value);
                isValid = true;
            }catch (NumberFormatException ex) {
                System.out.println("Value must be a number.");
            }
        } while(!isValid);
        return result;
    }

    private int readInt(String prompt, int min, int max) {
        int result = 0;
        do{
            result = readInt(prompt);
            if(result < min || result > max) {
                System.out.printf("Value must be between %s and %s.%n", min, max);
            }
        }while(result < min || result > max);
        return result;
    }

    private String readRequiredString(String prompt) {
        String result = null;
        do{
            result = readString(prompt).trim();
            if(result.length() == 0) {
                System.out.println("Value is required.");
            }
        }while(result.length() == 0);
        return result;
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return console.nextLine();
    }

    public void enterToContinue(){
        readString("Press [Enter] to continue.");
    }
}
