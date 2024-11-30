package ku.cs.controllers.facultyStaff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ku.cs.models.ApproveFacultyStaff;
import ku.cs.models.ApproveFacultyStaffList;
import ku.cs.models.Faculty;
import ku.cs.services.ApproveFacultyStaffListDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.FacultyListFileDatasource;

import java.io.IOException;

public class EditApproveFacultyStaffController {
    @FXML private TextField nameId, positionId, facultyId;

    @FXML private Label errorLabel1;
    @FXML private Label errorLabel2;
    @FXML private Label errorLabel3;
    @FXML private Label errorLabel4;
    @FXML private Button confirmButton;
    @FXML private ChoiceBox<String> roleChoiceBox;
    @FXML private ChoiceBox<String> facultyChoiceBox;
    private ApproveFacultyStaffList approveFacultyStaffList;
    private Datasource<ApproveFacultyStaffList> datasource;
    private FacultyListFileDatasource facultyDatasource;
    @FXML private Label position;
    @FXML
    public void initialize() {
        clearErrorLabels();
        positionId.setVisible(false);
        datasource = new ApproveFacultyStaffListDatasource("data", "approveFacultyStaff.csv");
        approveFacultyStaffList = datasource.readData();

        confirmButton.setOnAction(actionEvent -> { editApproveFacultyStaff(); });
        roleChoiceBox.setOnAction(actionEvent -> { handlePositionSelection(); });
        loadRoleChoices();
        loadFacultyChoices();
    }

    private void editApproveFacultyStaff() {
        String name = nameId.getText().trim();
        String role = roleChoiceBox.getValue(); // ค่าที่เลือกจาก role
        String faculty = facultyChoiceBox.getValue(); // ดึงค่า faculty ที่เลือก
        String position = positionId.getText().trim();

        // ตรวจสอบว่ามีการเลือกค่า faculty และ role หรือไม่
        if (role == null || faculty == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "please select role and faculty");
            return;
        }

        // ตรวจสอบว่าข้อมูลถูกต้องก่อนแก้ไข
        if (isInputValid(name, role, faculty, position)) {
            // แก้ไขข้อมูล faculty staff
            if (updateFacultyStaff(name, position, faculty, role)) {
                datasource.writeData(approveFacultyStaffList); // เขียนข้อมูลที่ถูกแก้ไขกลับไปยังไฟล์ CSV
                showAlert(Alert.AlertType.INFORMATION, "Success", "Faculty staff updated successfully");
                clearErrorLabels();
                clearTextFields();
            }
        }
    }
    private void loadRoleChoices() {
        ObservableList<String> roles = FXCollections.observableArrayList("คณบดี", "รองคณบดี", "รักษาการแทนคณบดี");
        roleChoiceBox.setItems(roles);
    }

    private void loadFacultyChoices() {
        facultyDatasource = new FacultyListFileDatasource("data", "faculty.csv");
        ObservableList<String> facultyNames = FXCollections.observableArrayList();

        for (Faculty faculty : facultyDatasource.readData().getFaculties()) {
            facultyNames.add(faculty.getFacultyName());
        }

        facultyChoiceBox.setItems(facultyNames);
    }
    private void handlePositionSelection() {
        String selectedRole = roleChoiceBox.getValue();
        if ("รองคณบดี".equals(selectedRole)) {
            position.setText("ฝ่าย");
            positionId.setVisible(true); // แสดง TextField ฝ่ายเมื่อเลือก "รองคณบดี"
        } else {
            position.setText("");
            positionId.setVisible(false); // ซ่อน TextField ฝ่ายเมื่อไม่ได้เลือก "รองคณบดี"
            positionId.clear(); // ล้างค่า TextField ฝ่ายเมื่อซ่อน
        }
    }


    private boolean isInputValid(String name, String role, String faculty, String position) {
        clearErrorLabels(); // ล้างข้อความแสดง error ก่อนตรวจสอบ
        boolean isValid = true;

        if (name == null || name.isEmpty()) {
            setError(errorLabel1, "Please enter name");
            isValid = false;
        }
        if (role == null || role.isEmpty()){
            setError(errorLabel2, "Please select role");
            isValid = false;
        }
        if (faculty == null || faculty.isEmpty()) {
            setError(errorLabel3, "Please select faculty");  // แก้ข้อความเป็น "Please select faculty"
            isValid = false;
        }
        if ("รองคณบดี".equals(role) && position.isEmpty()) {
            setError(errorLabel2, "Please enter position for รองคณบดี");
            isValid = false;
        }

        return isValid;
    }


    private boolean updateFacultyStaff(String name, String position, String faculty, String role) {
        for (ApproveFacultyStaff staff : approveFacultyStaffList.getApproveFacultyStaffList()) {
            if (staff.getName().equals(name)) {
                staff.setPosition(position);
                staff.setFaculty(faculty);
                staff.setRole(role);
                return true;
            }
        }
        setError(errorLabel4, "No matching faculty staff found");
        return false;
    }

    private void setError(Label label, String message) {
        clearErrorLabels();
        label.setText(message);
    }

    private void clearErrorLabels() {
        errorLabel1.setText("");
        errorLabel2.setText("");
        errorLabel3.setText("");
        errorLabel4.setText("");
        position.setText("");
    }
    private void clearTextFields(){
        nameId.clear();
        positionId.clear();
        facultyId.clear();
    }



    private void showAlert(Alert.AlertType alertType, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void onBackButtonClick(){
        try{
            FXRouter.goTo("approve-faculty-staff");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onAddFacultyStaffButtonClick(){
        try{
            FXRouter.goTo("addApproveFacultyStaff");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
