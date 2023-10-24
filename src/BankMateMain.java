import loan.Loans;

import java.util.Scanner;
public class BankMateMain {
    static Scanner choiceInput = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("\n========================================\nW e l c o m e    t o    B a n k M a t e\n\t\tyour financial companion\n========================================");
        System.out.println("   [1] Create Account\n   [2] Loans\n   [3] Withdrawal\n   [4] Deposit\n   [5] Transfers");
        System.out.print("\nEnter Choice: ");
        String choice = choiceInput.nextLine();

        choiceHandler(choice);
    }

    static void choiceHandler(String choice) {
        switch (choice) {
            case "1":
                System.out.println("Create Account?");
                break;
            case "2":
                new Loans().displayHeader();
                new Loans().displayOptions();
                break;
            case "3":
                System.out.println("Withdrawal?");
                break;
            case "4":
                System.out.println("Deposit?");
                break;
            case "5":
                System.out.println("Transfers?");
                break;
            default:
                System.out.println("Wrong choice. Try again later.");
        }
    }
}
