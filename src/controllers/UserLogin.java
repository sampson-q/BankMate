package controllers;

import dbConnectivity.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class UserLogin {
    private static final Logger logger = Logger.getLogger(UserLogin.class.getName());
    Controller controller = new Controller();

    public boolean validateUser(String accountNumber, String pinPassword) {
        // Hash the provided PIN code
        String hashedPin = controller.hashPin(pinPassword);

        if (hashedPin == null) {
            // Hashing failed; handle the error
            return false;
        }

        String sql = "SELECT * FROM users WHERE account_number = ? AND pin_code = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountNumber);
            preparedStatement.setString(2, hashedPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Check if a matching user is found
        } catch (SQLException e) {
            // Log the exception instead of printing the stack trace
            logger.log(java.util.logging.Level.SEVERE, "An error occurred during user login:", e);
            return false;
        }
    }
}
