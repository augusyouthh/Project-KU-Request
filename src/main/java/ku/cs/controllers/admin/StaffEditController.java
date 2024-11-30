package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.*;
import ku.cs.services.FXRouter;
import ku.cs.services.FacultyListFileDatasource;
import ku.cs.services.MajorListFileDatasource;
import ku.cs.services.UserListFileDatasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class StaffEditController implements Sidebar {

    @FXML
    private Label userTextLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label facultyLabel;
    @FXML
    private Label IdorDepartmentLabel;

    @FXML
    private Circle imagecircle;
    @FXML
    private Pane editStaffPane;
    @FXML
    private ChoiceBox<String> facultyChoiceBox;
    @FXML
    private ChoiceBox<String> majorChoiceBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField advisorIdTextfield;
    @FXML
    private Label advisorIdLabel;
    @FXML
    private Label majorChoiceBoxLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Circle imagecircleuser;

    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar


    private UserList userList;
    private UserListFileDatasource datasource;
    private User user;
    private User userdetail;
    private FacultyListFileDatasource facultyDatasource;
    private MajorListFileDatasource majorDatasource;

    public void initialize() {
        errorLabel.setText("");
        Object data = FXRouter.getData();
        if (data instanceof Pair) {
            Pair<User, User> userPair = (Pair<User, User>) data;
            user = userPair.getKey();
            userdetail = userPair.getValue();
        }
        displayUserInfo(userdetail);
        editStaffPane.setVisible(false);
        datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();
        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircleuser.setFill(new ImagePattern(new Image(url)));
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});
    }

    private void displayUserInfo(User user) {
        nameLabel.setText("ชื่อ: " + user.getName());
        usernameLabel.setText("ชื่อผู้ใช้: " + user.getUsername());
        facultyLabel.setText("คณะ: " + user.getFaculty());

        if (user.getRole().equals("student")) {
            userTextLabel.setText("ข้อมูลนิสิต");
        } else if (user.getRole().equals("advisor")) {
            userTextLabel.setText("ข้อมูลอาจารย์ที่ปรึกษา");
        } else if (user.getRole().equals("facultyStaff")) {
            userTextLabel.setText("ข้อมูลเจ้าหน้าที่คณะ");
            IdorDepartmentLabel.setText("");
        } else if (user.getRole().equals("majorStaff")) {
            userTextLabel.setText("ข้อมูลเจ้าหน้าที่ภาควิชา");
        }

        if (!user.getRole().equals("facultyStaff")) {
            IdorDepartmentLabel.setText("ภาควิชา: " + user.getMajor());

        }
        if (user.getRole().equals("facultyStaff")) {
            majorChoiceBoxLabel.setVisible(false);

        }if (!user.getRole().equals("advisor")) {
            advisorIdTextfield.setVisible(false);
            advisorIdLabel.setVisible(false);

        }

        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircle.setFill(new ImagePattern(new Image(url)));
    }

    private void loadMajorChoices(String facultyId) {
        majorDatasource = new MajorListFileDatasource("data", "major.csv");
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
    public void enterEditButtonClick() {
        User findUser = userList.findUserByUsername(userdetail.getUsername());
        String newName = nameTextField.getText();
        if (!newName.isEmpty()) {
            if (userList.findUserByName(newName) != null) {
                errorLabel.setText("Username " + newName + " มีอยู่แล้ว");
                return;
            }
            findUser.setName(newName);
        }

        String newUsername = usernameTextField.getText();
        if (!newUsername.isEmpty()) {
            if (userList.findUserByUsername(newUsername) != null) {
                errorLabel.setText("Username " + newUsername + " มีอยู่แล้ว");
                return;
            }
            findUser.setUsername(newUsername);
        }

        String newPassword = passwordTextField.getText();
        if (!newPassword.isEmpty()) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            findUser.setPassword(hashedPassword);
        }

        String newId = advisorIdTextfield.getText();
        if (!newId.isEmpty()) {
            if (userList.findUserById(newId) != null) {
                errorLabel.setText("Id " + newId + " มีอยู่แล้ว");
                return;
            }
            findUser.setId(newId);
        }

        String selectedFaculty = facultyChoiceBox.getValue();
        if (selectedFaculty != null) {
            findUser.setFaculty(selectedFaculty);
        }

        String selectedMajor = majorChoiceBox.getValue();
        if (selectedMajor != null) {
            findUser.setMajor(selectedMajor);
        }



        datasource.writeData(userList);

        displayUserInfo(findUser);
        editStaffButtonClick();
        clearInputFields();
        editStaffPane.setVisible(false);
    }
    @FXML
    private void editStaffButtonClick() {
        editStaffPane.setVisible(!editStaffPane.isVisible());
        loadFacultyChoices();
        majorChoiceBox.setDisable(true);
        if (userdetail.getRole().equals("advisor")) {
            majorChoiceBox.setVisible(false);
        }
        if (userdetail.getRole().equals("facultyStaff")) {
            majorChoiceBox.setVisible(false);
        } else {
            majorChoiceBox.setVisible(true);
            facultyChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    majorChoiceBox.setDisable(false);
                } else {
                    majorChoiceBox.setDisable(true);
                }
            });
        }

    }
    private void clearInputFields() {
        nameTextField.clear();
        usernameTextField.clear();
        passwordTextField.clear();
        advisorIdTextfield.clear();
        facultyChoiceBox.getSelectionModel().clearSelection();
        majorChoiceBox.getSelectionModel().clearSelection();

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
    @FXML
    public void onUserProfileButton() {
        try {
            FXRouter.goTo("user-profile",user);
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