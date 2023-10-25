package home;

import loan.Loans;
import withdrawal.Withdrawal;

import java.util.Scanner;

public class Dashboard {
    Scanner dashboardInput = new Scanner(System.in);
    public void displayDashboard() {
        System.out.println("\n========================================\nW e l c o m e    t o    B a n k M a t e\n\t\tyour financial companion\n==========> D A S H B O A R D <=========");
        System.out.println("   [1] Loans\n   [2] Withdrawal\n   [3] Deposit\n   [4] Transfers");

        System.out.print("Enter choice: ");
        String choice = dashboardInput.nextLine();

        choiceHandler(choice);
    }

    void choiceHandler(String choice) {
        switch (choice) {
            case "1":
                new Loans().displayHeader();
                new Loans().displayOptions();
                break;
            case "2":
                new Withdrawal().displayHeader();
                new Withdrawal().displayOptions();
                break;
            case "3":
                System.out.println("Deposit?");
                break;
            case "4":
                System.out.println("Transfers?");
                break;
            default:
                System.out.println("Wrong choice. Try again later.");
        }
    }
}
