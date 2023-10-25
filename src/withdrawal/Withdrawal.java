package withdrawal;
import java.util.Scanner;

public class Withdrawal {
    Scanner withdrawalInput = new Scanner(System.in);
    public void displayHeader() {
        System.out.println("\n========================================\nW e l c o m e    t o    B a n k M a t e\n\t\tyour financial companion\n========> W I T H D R A W A L <=========");
    }

    public void displayOptions() {
        System.out.println("Account Balance: GH₵0.00\n");
        System.out.print("Enter Withdrawal amount: ");

        Double withdrawAmount = withdrawalInput.nextDouble();
        String pinCode = withdrawalInput.nextLine();

        System.out.print("You have withdrawn ");
        System.out.print(withdrawAmount);
        System.out.print(". Your new balance is: GH₵0.00");
    }
}
