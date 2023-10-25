package withdrawal;
import controllers.Controller;
import dbConnectivity.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Statement;


public class Withdrawal {
    Controller controller = new Controller();
    Scanner withdrawalInput = new Scanner(System.in);
    public void displayHeader() {
        System.out.println("\n========================================\nW e l c o m e    t o    B a n k M a t e\n\t\tyour financial companion\n========> W I T H D R A W A L <=========");
    }

    public void displayOptions() {
        System.out.println("Account Balance: GH₵" + controller.getActiveAccountBalance());

        System.out.print("Enter Withdrawal amount: ");
        Double withdrawAmount = withdrawalInput.nextDouble();

        System.out.print("Enter pin code: ");
        String pinCode = withdrawalInput.nextLine();

        System.out.print("You have withdrawn ");
        System.out.print(withdrawAmount);
        System.out.print(". Your new balance is: GH₵0.00");
    }

    void handleWithdrawal(BigDecimal withdrawalAmount, String pinCode) {
        String pinCodeHash = controller.hashPin(pinCode);

        String validateAccountSQL = "SELECT * FROM users WHERE phone_number = ? AND pin_code = ?";
        String insertTransactionSQL = "INSERT INTO transactions (account_id, amount, timestamp) VALUES (?, ?, NOW())";
        String updateAccountBalanceSQL = "UPDATE accounts SET account_balance = ? WHERE account_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement validateStatement = connection.prepareStatement(validateAccountSQL);
             PreparedStatement insertStatement = connection.prepareStatement(insertTransactionSQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement updateStatement = connection.prepareStatement(updateAccountBalanceSQL)) {

            validateStatement.setString(1, controller.getActiveAccountNumber());
            validateStatement.setString(2, pinCodeHash);
            ResultSet resultSet = validateStatement.executeQuery();

            if (resultSet.next()) {
                BigDecimal activeAccountBalance = controller.getActiveAccountBalance();
                if (withdrawalAmount.compareTo(activeAccountBalance) <= 0) {
                    // Perform withdrawal
                    BigDecimal newAccountBalance = activeAccountBalance.subtract(withdrawalAmount);

                    // Update the account balance in the database or controller
                    updateStatement.setBigDecimal(1, newAccountBalance);
                    updateStatement.setInt(2, controller.getActiveAccountID());
                    updateStatement.executeUpdate();

                    // Insert the transaction record
                    insertStatement.setInt(1, controller.getActiveAccountID()); // Assuming account_id is an integer
                    insertStatement.setBigDecimal(2, withdrawalAmount);
                    insertStatement.executeUpdate();

                    // Commit the transaction
                    connection.commit();
                } else {
                    // Insufficient balance, handle accordingly
                }
            } else {
                // Account not found, handle accordingly
            }
        } catch (SQLException e) {
            // Handle SQL exception, rollback the transaction if necessary
            System.out.println("Transaction failed: " + e.getMessage());
        }
    }


}
