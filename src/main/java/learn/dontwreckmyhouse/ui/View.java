package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.domain.Result;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Component;

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
        if(reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found");
        } else {
            List<Reservation> reservationsList = reservations.stream().sorted().toList();
            reservationsList.forEach(System.out::println);
        }
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

    public void displayResult(Result result) {
        if(result.isSuccess()){
            displayText("Success!");
        } else{
            displayText("Error: ");
            for(String error : result.getMessages()){
                System.out.println(error);
            }
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
}
