package ku.cs.controllers.advisor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.models.User;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainAdvisorController implements Sidebar {

    @FXML
    private Label errorLabel;
    @FXML
    private TableView<Student> studentAdvisorTableView;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButtonClick;
    @FXML
    private Label usernameLabel;

    private StudentList studentList;
    private ObservableList<Student> studentObservableList;

    private User user;

    private String selectedStudentId;

    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar

    @FXML
    public void initialize() {
        errorLabel.setText("");

        StudentListFileDatasource datasource = new StudentListFileDatasource("data", "student-info.csv");
        studentList = datasource.readData();

        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;
        }

        updateUI();

        StudentList filteredStudentList = new StudentList();
        for (Student student : studentList.getStudents()) {
            String advisorID = student.getAdvisorID();
            if (advisorID != null && advisorID.equals(user.getId())) {
                filteredStudentList.addStudent(student);
            }
        }

        showTable(filteredStudentList);

        studentAdvisorTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observableValue, Student oldValue, Student newValue) {
                if (newValue != null) {
                    selectedStudentId = newValue.getId();
                    try {
                        FXRouter.goTo("advisor-appeal-page", new Pair<>(user, selectedStudentId));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        searchButtonClick.setOnAction(actionEvent -> searchStudent());
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});

    }

    private void showTable(StudentList studentList) {

        TableColumn<Student, String> idColumn = new TableColumn<>("Student ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> facultyColumn = new TableColumn<>("Faculty");
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("faculty"));

        TableColumn<Student, String> majorColumn = new TableColumn<>("Major");
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));

        TableColumn<Student, String> advisorIDColumn = new TableColumn<>("Advisor ID");
        advisorIDColumn.setCellValueFactory(new PropertyValueFactory<>("advisorID"));

        studentAdvisorTableView.getColumns().clear();
        studentAdvisorTableView.getColumns().addAll(idColumn, nameColumn, facultyColumn, majorColumn, advisorIDColumn);

        studentObservableList = FXCollections.observableArrayList(studentList.getStudents());


        studentAdvisorTableView.setItems(studentObservableList);
    }

    private void searchStudent() {
        String searchQuery = searchTextField.getText().trim();
        if (searchQuery.isEmpty()) {
            studentAdvisorTableView.setItems(studentObservableList);
            errorLabel.setText("Please enter search criteria.");
            return;
        }

        clearErrorLabels();

        List<Student> foundStudents = new ArrayList<>();

        // Search by student ID
        Student foundStudent = studentList.findStudentById(searchQuery);
        if (foundStudent != null) {
            foundStudents.add(foundStudent);
        }

        // Search by student name if no student ID match
        if (foundStudents.isEmpty()) {
            foundStudent = studentList.findStudentByName(searchQuery);
            if (foundStudent != null) {
                foundStudents.add(foundStudent);
            }
        }

        // Search by major ID
        if (foundStudents.isEmpty()) {
            foundStudents = studentList.findMajorByID(searchQuery);
        }

        // Search by faculty ID
        if (foundStudents.isEmpty()) {
            foundStudents = studentList.findFacultyByID(searchQuery);
        }

        // Filter based on advisor's ID
        List<Student> filteredStudents = foundStudents.stream()
                .filter(student -> student.getAdvisorID().equals(user.getId()))
                .collect(Collectors.toList());

        if (!filteredStudents.isEmpty()) {
            studentAdvisorTableView.setItems(FXCollections.observableArrayList(filteredStudents));
        } else {
            studentAdvisorTableView.getItems().clear();
            errorLabel.setText("No matching student found.");
        }

        clearTextFiled();
    }

    private void clearErrorLabels() {

        errorLabel.setText("");
    }

    private void clearTextFiled(){
        searchTextField.clear();
    }



    private void updateUI() {
        if (user != null) {
            usernameLabel.setText(user.getUsername());

        }
    }

    @FXML
    public void onPictureClick() {
        navigateTo("user-profile", user);
    }

    private void navigateTo(String route, Object data) {

        try {
            FXRouter.goTo(route, data); // ส่งข้อมูลไปยัง route ที่กำหนด
        } catch (IOException e) {
            System.err.println("Navigation to " + route + " failed: " + e.getMessage());
        }
    }

    @Override
    public void loadSidebar() {
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