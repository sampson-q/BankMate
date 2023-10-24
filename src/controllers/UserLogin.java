package controllers;

import dbConnectivity.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserLogin {
    private static final Logger logger = Logger.getLogger(UserLogin.class.getName());

    public boolean validateUser(int accountNumber, String pinPassword) {
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
}
