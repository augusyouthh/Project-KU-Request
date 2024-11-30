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

public class AddApproveFacultyStaffController {
    @FXML
    private Button confirmButton;
    @FXML
    private Label errorLabel1;
    @FXML
    private Label errorLabel2;
    @FXML
    private Label errorLabel3;
    @FXML
    private Label errorLabel4;
    @FXML
    private Label position;
    @FXML
    private TextField nameId,positionTextFieldId;
    @FXML
    private ChoiceBox<String> roleChoiceBox, facultyChoiceBox;
    private FacultyListFileDatasource facultyDatasource;
    private Datasource<ApproveFacultyStaffList> datasource;
    private ApproveFacultyStaffList approveFacultyStaffList;

    @FXML
    public void initialize() {
        clearErrorLabels();
        positionTextFieldId.setVisible(false); // ซ่อน text field ไม่ให้แสดงตอนแรก
        datasource = new ApproveFacultyStaffListDatasource("data", "approveFacultyStaff.csv");
        approveFacultyStaffList = datasource.readData();
        confirmButton.setOnAction(actionEvent -> {addApproveFacultyStaff();});
        roleChoiceBox.setOnAction(actionEvent -> {handlePositionSelection();});
        loadRoleChoices();
        loadFacultyChoices();
    }

    private void addApproveFacultyStaff() {
        String name = nameId.getText().trim();
        String role = roleChoiceBox.getValue(); // ค่าที่เลือกจาก role
        String faculty = facultyChoiceBox.getValue(); // ดึงค่า faculty ที่เลือก
        String position = positionTextFieldId.getText().trim();

        // ตรวจสอบว่ามีการเลือกค่า faculty และ role หรือไม่
        if (role == null || faculty == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "กรุณาเลือกตำแหน่งและคณะ");
            return;
        }

        if (isInputValid(name, role, faculty, position)) {
            approveFacultyStaffList.addNewApproveFacultyStaff(name, role, faculty, position);
            datasource.writeData(approveFacultyStaffList);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Faculty staff added successfully");
            clearErrorLabels();
            clearTextFiled();
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
            positionTextFieldId.setVisible(true); // แสดง TextField ฝ่ายเมื่อเลือก "รองคณบดี"
        } else {
            position.setText("");
            positionTextFieldId.setVisible(false); // ซ่อน TextField ฝ่ายเมื่อไม่ได้เลือก "รองคณบดี"
            positionTextFieldId.clear(); // ล้างค่า TextField ฝ่ายเมื่อซ่อน
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

        // ตรวจสอบว่าชื่อไม่ซ้ำใน database
        for (ApproveFacultyStaff staff : approveFacultyStaffList.getApproveFacultyStaffList()) {
            if (staff.getName().equals(name)){
                setError(errorLabel4, "Data has already been in database");
                isValid = false;
            }
        }

        return isValid;
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

    private void clearTextFiled(){
        nameId.clear();
        positionTextFieldId.clear();
    }

    public void showAlert(Alert.AlertType alertType, String title, String message){
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


}
