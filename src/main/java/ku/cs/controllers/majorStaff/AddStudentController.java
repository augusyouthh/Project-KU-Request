package ku.cs.controllers.majorStaff;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.Alert.AlertType;

public class AddStudentController implements Sidebar {
    @FXML
    private TextField advisorIdTextfield;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField idTextField;
    @FXML
    private Label addStudentLabel;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar
    @FXML private Circle imagecircle;


    private User user;
    private StudentList studentList;
    private PreRegisterStudentListFileDatasource studentFileDatasource;
    @FXML
    public void initialize() {
        addStudentLabel.setText("");
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;
        }
        studentFileDatasource = new PreRegisterStudentListFileDatasource("data", "studentPreRegister.csv");
        studentList = studentFileDatasource.readData();
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});
        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircle.setFill(new ImagePattern(new Image(url)));

    }

    @FXML
    public void addStaffButtonClick() {
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        String id = idTextField.getText();
        String advisorId = advisorIdTextfield.getText().isEmpty() ? null : advisorIdTextfield.getText();

        if (name.isEmpty() || email.isEmpty() || id.isEmpty() ){
            addStudentLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
            return;
        }

        if (studentList.findStudentByName(name) != null) {
            addStudentLabel.setText("ชื่อ " + name + " มีอยู่แล้ว");
            return;
        }
        if (studentList.findStudentByEmail(email) != null) {
            addStudentLabel.setText("Email " + email + " มีอยู่แล้ว");
            return;
        }
        if (studentList.findStudentById(id) != null) {
            addStudentLabel.setText("ID " + id + " มีอยู่แล้ว");
            return;
        }

        Student newStudent = new Student(name, id, email, user.getFaculty(), user.getMajor(), advisorId);

        studentList.addStudent(newStudent);
        PreRegisterStudentListFileDatasource studentDatasource = new PreRegisterStudentListFileDatasource("data", "studentPreRegister.csv");
        studentDatasource.writeData(studentList);
        addStudentLabel.setText("เพิ่มผู้ใช้สำเร็จ: " + name);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("สำเร็จ");
        alert.setHeaderText(null);
        alert.setContentText("เพิ่มผู้ใช้สำเร็จ: " + name);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                clearInputFields();
            }
        });;
    }
    private void clearInputFields() {
        nameTextField.clear();
        idTextField.clear();
        emailTextField.clear();
        advisorIdTextfield.clear();
        addStudentLabel.setText("");


    }

    //Sidebar
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

    //Navbar button
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
    public void onHomeButton(){
        navigateTo("departmentStaff", user);
    }
    @FXML
    public void onUserProfileButton(){
        navigateTo("user-profile", user);
    }

    private void navigateTo(String route, Object data) {
        try {
            FXRouter.goTo(route, data);
        } catch (IOException e) {
            System.err.println("Navigation to " + route + " failed: " + e.getMessage());
        }
    }
}