package ku.cs.controllers.admin;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AddStaffController implements Sidebar {

    @FXML
    private TextField advisorIdTextfield;
    @FXML
    private ChoiceBox<String> facultyChoiceBox;
    @FXML
    private ChoiceBox<String> majorChoiceBox;
    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label advisorIdLabel;
    @FXML
    private Label majorChoiceBoxLabel;
    @FXML
    private Label addStaffLabel;
    @FXML
    private Circle imagecircleuser;

    private UserList userList;
    private User user;
    private FacultyListFileDatasource facultyDatasource;

    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar

    @FXML
    public void initialize() {
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;

        }
        addStaffLabel.setText("");
        loadRoleChoices();
        loadFacultyChoices();

        roleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleRoleSelection(newValue);
        });
        loadData();
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});
        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircleuser.setFill(new ImagePattern(new Image(url)));
    }


    private void loadData() {
        Datasource<UserList> userDatasource = new UserListFileDatasource("data", "user.csv");
        userList = userDatasource.readData();
    }


    private void loadRoleChoices() {
        roleChoiceBox.getItems().clear();
        roleChoiceBox.getItems().addAll("อาจารย์ที่ปรึกษา", "เจ้าหน้าที่คณะ", "เจ้าหน้าที่ภาควิชา");
    }

    private void handleRoleSelection(String role) {
        if ("เจ้าหน้าที่คณะ".equals(role)) {
            majorChoiceBoxLabel.setVisible(false);
            majorChoiceBox.setVisible(false);
        } else {
            majorChoiceBoxLabel.setVisible(true);
            majorChoiceBox.setVisible(true);
        }

        if (!"อาจารย์ที่ปรึกษา".equals(role)) {
            advisorIdLabel.setVisible(false);
            advisorIdTextfield.setVisible(false);
        } else {
            advisorIdLabel.setVisible(true);
            advisorIdTextfield.setVisible(true);
        }

    }

    private void loadMajorChoices(String facultyId) {
        MajorListFileDatasource majorDatasource = new MajorListFileDatasource("data", "major.csv");
        majorChoiceBox.getItems().clear();
        MajorList majorList = majorDatasource.readData();
        List<Major> filteredMajors = majorList.filterMajorsByFaculty(facultyId);

        ObservableList<String> majorNames = FXCollections.observableArrayList();
        for (Major major : filteredMajors) {
            majorNames.add(major.getMajorName());
        }
        majorChoiceBox.getItems().addAll(majorNames);
    }


    private void loadFacultyChoices() {
        facultyDatasource = new FacultyListFileDatasource("data", "faculty.csv");
        facultyChoiceBox.getItems().clear();

        ObservableList<String> facultyNames = FXCollections.observableArrayList();
        for (Faculty faculty : facultyDatasource.readData().getFaculties()) {
            facultyNames.add(faculty.getFacultyName());
        }
        facultyChoiceBox.getItems().addAll(facultyNames);

        facultyChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                String selectedFacultyId = facultyDatasource.readData().getFaculties().stream()
                        .filter(faculty -> faculty.getFacultyName().equals(newValue))
                        .findFirst()
                        .map(Faculty::getFacultyId)
                        .orElse(null);

                if (selectedFacultyId != null) {
                    majorChoiceBox.setDisable(false);

                    processSelectedFacultyId(selectedFacultyId);
                }
            } else {
                majorChoiceBox.setDisable(true);
            }
        });
    }

    private void processSelectedFacultyId(String facultyId) {
        loadMajorChoices(facultyId);
    }

    @FXML
    public void addStaffButtonClick() {
        String name = nameTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String role = roleChoiceBox.getValue();
        String faculty = facultyChoiceBox.getValue();
        String major = majorChoiceBox.getValue();
        String advisorId = advisorIdTextfield.getText();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || role == null || faculty == null) {
            addStaffLabel.setText("Please fill in all fields.");
            return;
        }
        if (role.equals("อาจารย์ที่ปรึกษา")) {
            role = "advisor";
        } else if (role.equals("เจ้าหน้าที่คณะ")) {
            role = "facultyStaff";
        } else if (role.equals("เจ้าหน้าที่ภาควิชา")) {
            role = "majorStaff";
        }

        if (userList.findUserByUsername(username) != null) {
            addStaffLabel.setText("ชื่อผู้ใช้ " + username + " มีอยู่แล้ว กรุณาใช้ชื่ออื่น");
            return;
        }

        if (userList.findUserByName(name) != null) {
            addStaffLabel.setText("ชื่อ " + name + " มีอยู่แล้ว กรุณาใช้ชื่ออื่น");
            return;
        }

        if (advisorId != null && !advisorId.isEmpty()) {
            User foundUser = userList.findUserById(advisorId);
            if (foundUser != null && advisorId.equals(foundUser.getId())) {
                addStaffLabel.setText("ID " + advisorId + " มีอยู่แล้ว กรุณาใช้ ID อื่น");
                return;
            }
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = null;

        if (role.equals("advisor")) {

            newUser = new User(name, username, hashedPassword, null, null, role, null, false, faculty, major, true, advisorId);
        } else if (role.equals("facultyStaff")) {
            newUser = new User(name, username, hashedPassword, null, null, role, null, false, faculty, null, true, null);
        } else if (role.equals("majorStaff")) {
            newUser = new User(name, username, hashedPassword, null, null, role, null, false, faculty, major, true, null);
        } else {
            throw new IllegalArgumentException("บทบาทไม่ถูกต้อง");
        }

        userList.addUser(newUser);
        UserListFileDatasource userDatasource = new UserListFileDatasource("data", "user.csv");
        userDatasource.writeData(userList);
        addStaffLabel.setText("เพิ่มผู้ใช้สำเร็จ: " + username);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("สำเร็จ");
        alert.setHeaderText(null);
        alert.setContentText("เพิ่มผู้ใช้สำเร็จ: " + username);


        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                initialize();
                clearInputFields();
            }
        });;
    }

    private void clearInputFields() {
        nameTextField.clear();
        usernameTextField.clear();
        passwordTextField.clear();
        advisorIdTextfield.clear();
        roleChoiceBox.getSelectionModel().clearSelection();
        facultyChoiceBox.getSelectionModel().clearSelection();
        majorChoiceBox.getSelectionModel().clearSelection();

    }




    @FXML
    public void onUserProfileButton() {
        try {
            FXRouter.goTo("user-profile",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void dashboardButtonClick() {
        try {
            FXRouter.goTo("dashboard",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void manageStaffdataButtonClick() {
        try {
            FXRouter.goTo("staff-table-admin",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void homeButtonClick() {
        try {
            FXRouter.goTo("main-admin",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onManageFacultyButtonClick() {
        try {
            FXRouter.goTo("faculty-data-admin",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
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