package controllers;

import dbConnectivity.DatabaseConnection;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Controller {
    String activeAccountNumber, activeAccountID, activeAccountName;
    Integer activeAccountLoanID;
    BigDecimal activeAccountBalance, activeAccountLoanBalance;
    private static final Logger logger = Logger.getLogger(UserSignup.class.getName());

    // Validate the user for signup
    public boolean validateUserSignup(int accountNumber, String pinPassword) {
        // Hash the provided PIN code
        String hashedPin = hashPin(pinPassword);

        if (hashedPin == null) {
            // Hashing failed; handle the error
            return false;
        }

        String sql = "SELECT * FROM users WHERE account_number = ? AND pin_code = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, accountNumber);
            preparedStatement.setString(2, hashedPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Check if a matching user is found
        } catch (SQLException e) {
            // Log the exception instead of printing the stack trace
            logger.log(java.util.logging.Level.SEVERE, "An error occurred during user login:", e);
            return false;
        }
    }

    // Method to hash the PIN code using SHA-256
    public String hashPin(String pinPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] pinBytes = pinPassword.getBytes();
            byte[] hashedPinBytes = md.digest(pinBytes);

            // Convert the hashed PIN to a hexadecimal string
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashedPinBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error hashing PIN code:", e);
            return null;
        }
    }

    // Method to validate the PIN code
    public boolean isValidPin(String pinPassword) {
        // PIN should be numeric and up to 4 digits
        return pinPassword.matches("^\\d{1,4}$");
    }

    // Method to check if a user with the same phone number already exists
    public boolean userExists(String phoneNumber) {
        String sql = "SELECT COUNT(*) FROM Users WHERE phone_number = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, phoneNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error checking if user exists:", e);
        }
        return false; // Default to false in case of an error
    }

    public String getActiveAccountID() {
        return activeAccountID;
    }

    public void setActiveAccountID() {
        String accountNumberSQL = "SELECT account_id FROM accounts WHERE phone_number = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(accountNumberSQL)) {
            preparedStatement.setString(1, getActiveAccountNumber());
            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                this.activeAccountID = resultSet.getString("account_id"); // set up account ID
            } else {
                System.out.println("Account not found for the given phone number.");
            }

            // Close resources
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "An error occurred during account number fetch:", e);
        }
    }

    public String getActiveAccountName() {
        return activeAccountName;
    }

    public void setActiveAccountName() {
        String fullNameSQL = "SELECT full_name FROM users WHERE phone_number = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(fullNameSQL)) {
            preparedStatement.setString(1, getActiveAccountNumber());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                this.activeAccountName = resultSet.getString("full_name");
            }

            // Close resources
            resultSet.close();
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "An error occurred during full name fetch:", e);
        }
    }

    public BigDecimal getActiveAccountBalance() {
        return activeAccountBalance;
    }

    public void setActiveAccountBalance() {
        String accountBalanceSQL = "SELECT account_balance FROM accounts WHERE phone_number = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(accountBalanceSQL)) {
            preparedStatement.setString(1, getActiveAccountNumber());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                this.activeAccountBalance = resultSet.getBigDecimal("account_balance");
            }

            // Close resources
            resultSet.close();
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "An error occurred during account balance fetch:", e);
        }

    }

    public BigDecimal getActiveAccountLoanBalance() {
        return activeAccountLoanBalance;
    }

    public void setActiveAccountLoanBalance() {
        String loanBalanceSQL = "SELECT L.loan_balance " +
                "FROM loans L " +
                "JOIN accounts A ON L.account_id = A.account_id " +
                "JOIN users U ON A.phone_number = U.phone_number " +
                "WHERE U.phone_number = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(loanBalanceSQL)) {
            preparedStatement.setString(1, getActiveAccountNumber());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                this.activeAccountLoanBalance = resultSet.getBigDecimal("loan_balance");
            }

            // Close resources
            resultSet.close();
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "An error occurred during loan balance fetch:", e);
        }
    }

    public Integer getActiveAccountLoanID() {
        return activeAccountLoanID;
    }

    public void setActiveAccountLoanID() {
        String loanIdSQL = "SELECT L.loan_id " +
                "FROM loans L " +
                "JOIN accounts A ON L.account_id = A.account_id " +
                "JOIN users U ON A.phone_number = U.phone_number " +
                "WHERE U.phone_number = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(loanIdSQL)) {
            preparedStatement.setString(1, getActiveAccountNumber());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                this.activeAccountLoanID = resultSet.getInt("loan_id");
            }

            // Close resources
            resultSet.close();
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "An error occurred during loan ID fetch:", e);
        }

    }

    public void setActiveAccountNumber(String activeAccountNumber) {
        this.activeAccountNumber = activeAccountNumber;
    }

    public String getActiveAccountNumber() {
        return this.activeAccountNumber;
    }
}
