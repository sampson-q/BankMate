package controllers;

import dbConnectivity.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserSignup {
    private static final Logger logger = Logger.getLogger(UserSignup.class.getName());

    // Method to check if a user with the same phone number already exists
    private boolean userExists(String phoneNumber) {
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

    // Method to create a new user
    public boolean createUser(String fullName, String phoneNumber, String pinPassword) {
        // Validate the PIN code
        if (!isValidPin(pinPassword)) {
            logger.warning("Invalid PIN code. PIN should be numeric and up to 4 digits.");
            return false;
        }

        // Hash the PIN code
        String hashedPin = hashPin(pinPassword);

        // Check if the user with the same phone number already exists
        if (userExists(phoneNumber)) {
            logger.warning("User with the same phone number already exists.");
            return false;
        }

        // If the user doesn't exist and the PIN code is valid, proceed with insertion
        String sql = "INSERT INTO Users (full_name, phone_number, pin_code) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, phoneNumber);
            preparedStatement.setString(3, hashedPin);
            return preparedStatement.executeUpdate() > 0; // Check if a row was inserted
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "An error occurred during user signup:", e);
            return false;
        }
    }

    // Method to hash the PIN code using SHA-256
    private String hashPin(String pinPassword) {
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
    private boolean isValidPin(String pinPassword) {
        // PIN should be numeric and up to 4 digits
        return pinPassword.matches("^\\d{1,4}$");
    }
}