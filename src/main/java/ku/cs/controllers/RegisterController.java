package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.*;
import org.mindrot.jbcrypt.BCrypt;


import java.io.IOException;

public class RegisterController {
    @FXML private Label errorLabel;
    @FXML private TextField nameTextField;
    @FXML private TextField usernameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField idTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private PasswordField confirmPasswordTextField;
    private StudentList studentList;
    private UserList userList;
    private UserListFileDatasource Userdatasource;
    private StudentListFileDatasource Studentdatasource;
    private PreRegisterStudentListFileDatasource Preregisterstudentlistfiledatasource;
    private  StudentList Preregisterstudentlist;
    @FXML
    private void initialize() {
        errorLabel.setText("");
        loadData();
    }

    private void loadData() {
        Studentdatasource = new StudentListFileDatasource("data", "student-info.csv");
        studentList = Studentdatasource.readData();

        Userdatasource = new UserListFileDatasource("data", "user.csv");
        userList = Userdatasource.readData();

        Preregisterstudentlistfiledatasource = new PreRegisterStudentListFileDatasource("data", "studentPreRegister.csv");
        Preregisterstudentlist = Preregisterstudentlistfiledatasource.readData();
    }



    private void checkRegister() {
        errorLabel.setText("");
        String name = nameTextField.getText();
        String id = idTextField.getText();
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();

        if (name.isEmpty()  || username.isEmpty() || id.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please enter all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        if (studentList.isExists(username, id)) {
            errorLabel.setText("Username or ID already exists.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        if(Preregisterstudentlist.isExists(name, id,email)) {
            Student findStudent = Preregisterstudentlist.findStudentById(id);
            Student newStudent = new Student(name, username, id, email, findStudent.getFaculty(), findStudent.getMajor(),findStudent.getAdvisorID());
            studentList.addStudent(newStudent);
            Datasource<StudentList> studentDatasource = new StudentListFileDatasource("data", "student-info.csv");
            studentDatasource.writeData(studentList);

            User newUser = new User(name, username, hashedPassword,"student",id,findStudent.getFaculty(),findStudent.getMajor());
            userList.addUser(newUser);
            UserListFileDatasource userDatasource = new UserListFileDatasource("data", "user.csv");
            userDatasource.writeData(userList);
        }
        else {
            errorLabel.setText("ไม่มีข้อมูลนิสิตในระบบ.");
            return;
        }


        try {
            FXRouter.goTo("login-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onRegisterButtonClick() {
        checkRegister();
    }
    @FXML
    private void onLoginButtonClick() {
        try {
            FXRouter.goTo("login-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
