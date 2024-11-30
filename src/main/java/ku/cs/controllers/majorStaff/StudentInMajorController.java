package ku.cs.controllers.majorStaff;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.models.User;
import ku.cs.services.*;

import java.io.File;
import java.io.IOException;

public class StudentInMajorController implements Sidebar {

    @FXML
    private TableView<Student> studentTableView;

    private StudentList studentList;
    @FXML
    private TextField searchUserTextfield;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar
    @FXML private Circle imagecircle;
    private User user;
    Datasource<StudentList> studentDatasource;
    StudentList studentlistInMajor;

    @FXML
    public void initialize() {
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;

        }
        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircle.setFill(new ImagePattern(new Image(url)));


        studentDatasource = new StudentListFileDatasource("data", "student-info.csv");
        studentList = studentDatasource.readData();
        studentlistInMajor = studentList.getStudentsListBYMajor(user.getMajor());
        showTable(studentlistInMajor);

        studentTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue observableValue, Student oldValue, Student newValue) {
            }
        });

        searchUserTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(newValue);
        });
        studentTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Student selectedStudent  = studentTableView.getSelectionModel().getSelectedItem();
            } else if (event.getClickCount() == 2) {
                Student selectedStudent  = studentTableView.getSelectionModel().getSelectedItem();
                if (selectedStudent != null) {
                    try {
                        Pair<User, Student> userStudentPair = new Pair<>(user, selectedStudent);
                        FXRouter.goTo("student-detail-major-staff", userStudentPair);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});
    }

    private void filterTable(String searchText) {
        StudentList filteredList = new StudentList();
        for (Student student : studentlistInMajor.getStudents()) {
            if (student.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    student.getUsername().toLowerCase().contains(searchText.toLowerCase()) ||
                    student.getEmail().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.addStudent(student);
            }
        }
        studentTableView.getItems().setAll(filteredList.getStudents());
    }

    private void showTable(StudentList studentList) {
        TableColumn<Student, String> idColumn = new TableColumn<>("Student ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> emailColumn = new TableColumn<>("email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Student, String> advisorColumn = new TableColumn<>("AdvisorId");
        advisorColumn.setCellValueFactory(new PropertyValueFactory<>("advisorID"));

        studentTableView.getColumns().clear();
        studentTableView.getColumns().addAll(idColumn, nameColumn, emailColumn, advisorColumn);

        studentTableView.getItems().setAll(studentList.getStudents());
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

    //Button to add student
    @FXML
    public void addStudentButtonClick(){
        navigateTo("add-student", user);
    }

}


