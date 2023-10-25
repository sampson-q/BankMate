package controllers;

import dbConnectivity.DatabaseConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Controller {
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
}
