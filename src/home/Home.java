package home;

import controllers.UserLogin;
import controllers.UserSignup;
import java.util.Scanner;

public class Home extends Dashboard {
    UserLogin userLogin = new UserLogin();
    UserSignup userSignup = new UserSignup();
    Scanner choiceInput = new Scanner(System.in);

    public void displayHeader() {
        System.out.println("\n========================================\nW e l c o m e    t o    B a n k M a t e\n\t\tyour financial companion\n================> H O M E <=============");
        System.out.println("   [1] Login\n   [2] Signup\n   [C] Cancel");
        System.out.print("\nEnter Choice: ");
        String choice = choiceInput.nextLine();

        choiceHandler(choice);
    }

    void userLogin() {
        System.out.println("Enter Account Number: ");
        String accNumber = choiceInput.nextLine();
        System.out.println("Enter Pin Code: ");
        String pinCode = choiceInput.nextLine();

        if (userLogin.validateUser(accNumber, pinCode)) {
            displayDashboard();
        } else {
            System.out.println("Error logging in.");
        }
    }

    void userSignup() {
        System.out.println("Enter Full Name: ");
        String fullName = choiceInput.nextLine();

        System.out.println("Enter Phone Number: ");
        String phoneNumber = choiceInput.nextLine();

        System.out.println("Enter Pin Code: ");
        String pinCode = choiceInput.nextLine();

        if (userSignup.createUser(fullName, phoneNumber, pinCode)) {
            System.out.println("Signup Success!");
            userLogin();
        } else {
            System.out.println("Error signing up.");
        }
    }

    void choiceHandler(String choice) {
        switch (choice) {
            case "1":
                userLogin();
                break;
            case "2":
                userSignup();
                break;
            case "C", "c":
                System.out.println("Thank you for using Bank Mate. I hope you enjoyed it.");
                break;
            default:
                System.out.println("Wrong choice. Try again later.");
        }
    }


}
