package controllers;

import dbConnectivity.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class UserSignup {
    Controller controller = new Controller();
    private static final Logger logger = Logger.getLogger(UserLogin.class.getName());

    // Method to create a new user
    public boolean createUser(String fullName, String phoneNumber, String pinPassword) {
        // Validate the PIN code
        if (!controller.isValidPin(pinPassword)) {
            logger.warning("Invalid PIN code. PIN should be numeric and up to 4 digits.");
            return false;
        }

        // Hash the PIN code
        String hashedPin = controller.hashPin(pinPassword);

        // Check if the user with the same phone number already exists
        if (controller.userExists(phoneNumber)) {
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

}