package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ku.cs.models.User;
import ku.cs.models.Student;

import ku.cs.models.UserList;
import ku.cs.models.StudentList;

import ku.cs.services.*;
import java.io.IOException;


public class LoginController {

    @FXML
    private Label errorLabel;
    @FXML
    private TextField UsernameTextField;
    @FXML
    private PasswordField PasswordTextField;
    private UserList userList;

    @FXML
    private void initialize() {
        errorLabel.setText("");

        Datasource<UserList> datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();
    }
    @FXML
    public void onLoginButtonClick() {
        try {
            handleLogin();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void handleLogin() throws IOException {
        String username = UsernameTextField.getText().trim();
        String password = PasswordTextField.getText().trim();

        User user = userList.authenticate(username, password);


        if (user == null) {
            errorLabel.setText("Username or password is incorrect. Please try again.");
            return;
        }
        userList.updateLastLogin(username);
        UserListFileDatasource userDatasource = new UserListFileDatasource("data", "user.csv");
        userDatasource.writeData(userList);
        navigateByRole(user);
    }




    private void navigateByRole(User user) throws IOException {
        switch (user.getRole()) {
            case "student":
                FXRouter.goTo("student", user);
                break;
            case "admin":
                FXRouter.goTo("main-admin", user);
                break;
            case "advisor":
                if (user.isFirstlogin()){
                    errorLabel.setText("Please change your password before your first login.");
                    return;
                }
                FXRouter.goTo("main-advisor", user);
                break;
            case "facultyStaff":
                if (user.isFirstlogin()){
                    errorLabel.setText("Please change your password before your first login.");
                    return;
                }
                FXRouter.goTo("facultyStaff", user);
                break;
            case "majorStaff":
                if (user.isFirstlogin()){
                    errorLabel.setText("Please change your password before your first login.");
                    return;
                }
                FXRouter.goTo("departmentStaff", user);
                break;
            default:
                errorLabel.setText("Invalid role. Please contact the administrator.");
                throw new IOException("Invalid role.");
        }
    }
    @FXML
    public void onRegisterButtonClick() {
        try {
            FXRouter.goTo("register");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onResetPasswordButton() {
        try {
            FXRouter.goTo("resetPassword");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}