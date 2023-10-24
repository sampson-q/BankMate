package loan;
import java.util.Scanner;

public class Loans {
    Scanner loanInput = new Scanner(System.in);
    public void displayHeader() {
        System.out.println("\n========================================\nW e l c o m e    t o    B a n k M a t e\n\t\tyour financial companion\n=============> L O A N S <==============");
    }

    public void displayOptions() {
        System.out.println("Loan Balance: GH₵0.00\n");
        System.out.println("   [1] Apply for Loan\n   [2] Loan Repay\n   [0] Back");
        System.out.print("\nEnter Choice: ");

        String choice = loanInput.nextLine();

        handleLoanChoice(choice);
    }

    void handleLoanChoice(String choice) {
        switch (choice) {
            case "1":
                loanApply();
                break;
            case "2":
                loanRepay();
                break;
            case "0":
                goBack();
                break;
            default:
                System.out.println("Wrong choice!");
        }
    }

    void loanApply() {
        System.out.print("Loan Amount: ");
        String loanAmount = loanInput.nextLine();
        System.out.println("Loan processed!");
    }
    void loanRepay() {
        System.out.print("Repay Amount: ");
        String repayAmount = loanInput.nextLine();
        System.out.println("Loan paid!");
    }
    void goBack() {
        System.out.println("Will work on go back");
    }
}
