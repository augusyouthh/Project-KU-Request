package ku.cs.controllers.majorStaff;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.File;
import java.io.IOException;

public class StudentDetailController implements Sidebar {
    @FXML
    private Label userTextLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label advisorLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label advisorIdLabel;
    @FXML
    private Circle imagecircle;
    @FXML
    private Pane editStaffPane;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField advisorIdTextfield;
    @FXML
    private TextField idTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar


    private UserList userList;
    private UserListFileDatasource datasource;
    private User userStudent;
    private User user;
    private Student student;
    private StudentList studentList;
    private StudentListFileDatasource studentFileDatasource;
    @FXML
    public void initialize() {
        errorLabel.setText("");
        datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();
        studentFileDatasource = new StudentListFileDatasource("data", "student-info.csv");
        studentList = studentFileDatasource.readData();
        editStaffPane.setVisible(false);
        Object data = FXRouter.getData();
        if (data instanceof Pair) {
            Pair<User, Student> userStudentPair = (Pair<User, Student>) data;
            user = userStudentPair.getKey();
            student = userStudentPair.getValue();
            displayUserInfo(student);

        }
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});
    }
    private void displayUserInfo(Student student1) {
        userTextLabel.setText("ข้อมูลนิสิต");
        nameLabel.setText(student1.getName());
        idLabel.setText(student1.getId());
        emailLabel.setText(student1.getEmail());
        User findAdvisor = userList.findUserById(student1.getAdvisorID());
        String advisorName;
        if (findAdvisor == null) {
            advisorName = "ยังไม่มีอาจารย์ที่ปรึกษา";
        } else {
            advisorName = findAdvisor.getName();
        }
        advisorLabel.setText(advisorName);
        advisorIdLabel.setText( student1.getAdvisorID());
        userStudent = userList.findUserByUsername(student1.getUsername());
        String imagePath = System.getProperty("user.dir") + File.separator + userStudent.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircle.setFill(new ImagePattern(new Image(url)));
    }
    @FXML
    public void enterEditButtonClick() {
        Student findStudent = studentList.findStudentByUsername(student.getUsername());
        User findUser = userList.findUserByUsername(student.getUsername());

        String newName = nameTextField.getText();
        if (!newName.isEmpty()) {
            findStudent.setName(newName);
            findUser.setName(newName);
        }

        String newId = idTextField.getText();
        if (!newId.isEmpty()) {
            if (studentList.findStudentById(newId) != null) {
                errorLabel.setText("Id " + newId + " มีอยู่แล้ว");
                return;
            }
            findStudent.setId(newId);
            findUser.setId(newId);
        }

        String newEmail = emailTextField.getText();
        if (!newEmail.isEmpty()) {
            if (studentList.findStudentByEmail(newEmail) != null) {
                errorLabel.setText("Email " + newEmail + " มีอยู่แล้ว");
                return;
            }
            findStudent.setEmail(newEmail);
        }


        if(!advisorIdTextfield.getText().isEmpty()){
            String newAdvisorId = advisorIdTextfield.getText().isEmpty() ? null : advisorIdTextfield.getText();
            findStudent.setAdvisorID(newAdvisorId);
        }

        studentFileDatasource.writeData(studentList);
        datasource.writeData(userList);

        nameTextField.clear();
        idTextField.clear();
        emailTextField.clear();
        advisorIdTextfield.clear();
        errorLabel.setText("");

        displayUserInfo(findStudent);

        editStaffPane.setVisible(false);
    }
    @FXML
    private void editStudentButtonClick() {
        editStaffPane.setVisible(!editStaffPane.isVisible());
    }

    @FXML
    public void onApproveMajorButton(){
        navigateTo("approve-major-list", user);
    }

    @FXML
    public void onStudentListButton(){
        navigateTo("student-in-major", user);
    }

    @FXML
    public void onAddEndorserButton(){
        navigateTo("add-major-endorser", user);
    }

    @FXML
    public void homeButtonClick(){
        navigateTo("departmentStaff", user);
    }

    private void navigateTo(String route, Object data) {
        try {
            FXRouter.goTo(route, data);
        } catch (IOException e) {
            System.err.println("Navigation to " + route + " failed: " + e.getMessage());
        }
    }

    @Override
    public void loadSidebar(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/other/sidebar.fxml"));
            AnchorPane loadedSidebar = loader.load();

            // ดึง SidebarController จาก FXML Loader
            SidebarController sidebarController = loader.getController();
            sidebarController.setSidebar(this); // กำหนด MainAdminController เป็น Sidebar เพื่อให้สามารถปิดได้

            sidebar = loadedSidebar; // กำหนด sidebar ที่โหลดเสร็จแล้ว
            sidebar.setVisible(false); // ปิด sidebar ไว้ในค่าเริ่มต้น
            mainPage.getChildren().add(sidebar); // เพิ่ม sidebar ไปยัง mainPage
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void toggleSidebar() {
        if (sidebar != null){
            sidebar.setVisible(!sidebar.isVisible());
            if (sidebar.isVisible()){
                sidebar.toFront(); //ให้ sidebar แสดงด้านหน้าสุด
            }
            else {
                sidebar.toBack();
            }
        }
    }

    @Override
    public void closeSidebar() {
        if (sidebar != null){
            sidebar.setVisible(false);
            sidebar.toBack();
        }
    }
}
