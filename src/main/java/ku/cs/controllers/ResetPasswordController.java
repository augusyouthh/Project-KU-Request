package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ku.cs.models.*;
import ku.cs.services.*;
import org.mindrot.jbcrypt.BCrypt;


import java.io.IOException;

public class ResetPasswordController {

    @FXML private Label errorLabel;
    @FXML private TextField usernameTextfield;
    @FXML private PasswordField oldPasswordTextfield;
    @FXML private PasswordField newPasswordTextfield;
    @FXML private PasswordField confirmPasswordTextfield;

    private UserList userList;

    @FXML
    private void initialize() {
        errorLabel.setText("");

        UserListFileDatasource userDatasource = new UserListFileDatasource("data", "user.csv");
        userList = userDatasource.readData();

    }

    @FXML
    private void onResetPasswordButtonClick() {
        try {
            resetPassword();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void onBackButton() {
        try {
            FXRouter.goTo("login-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetPassword() throws IOException {
        String username = usernameTextfield.getText().trim();
        String oldPassword = oldPasswordTextfield.getText().trim();
        String newPassword = newPasswordTextfield.getText().trim();
        String confirmPassword = confirmPasswordTextfield.getText().trim();

        if (username.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            errorLabel.setText("New password and confirmation do not match.");
            return;
        }

        User user = userList.findUserByUsername(username);
        if (user == null || !BCrypt.checkpw(oldPassword, user.getPassword())) {
            errorLabel.setText("Username or old password is incorrect.");
            return;
        }

        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(hashedNewPassword);

        if (user.getRole().equals("advisor") || user.getRole().equals("facultyStaff") || user.getRole().equals("majorStaff")) {
            user.setFirstlogin(false);
        }

        UserListFileDatasource userDatasource = new UserListFileDatasource("data", "user.csv");
        userDatasource.writeData(userList);

        errorLabel.setText("Password successfully reset.");
        FXRouter.goTo("login-page");
    }


}
