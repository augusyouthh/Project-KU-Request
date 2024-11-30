package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.Major;
import ku.cs.models.MajorList;
import ku.cs.models.User;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.MajorListFileDatasource;

import java.io.IOException;

public class addNewMajorDataController {
    @FXML
    private Label errorLabel1;
    @FXML
    private Label errorLabel2;
    @FXML
    private Label errorLabel3;
    @FXML
    private Label errorLabel4;
    @FXML
    private TextField facultyId;
    @FXML
    private TextField majorId;
    @FXML
    private TextField majorName;
    @FXML
    private Button okButton;

    private Datasource<MajorList> datasource;
    private MajorList majorList;
    private User user;

    @FXML
    public void initialize(){
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;

        }
        errorLabel1.setText("");
        errorLabel2.setText("");
        errorLabel3.setText("");
        errorLabel4.setText("");
        datasource = new MajorListFileDatasource("data", "Major.csv");
        majorList = datasource.readData();
        okButton.setOnAction(actionEvent -> {addNewMajorData();});

    }

    public void addNewMajorData() {
        String facultyID = facultyId.getText().trim();
        String majorID = majorId.getText().trim();
        String majorNAME = majorName.getText().trim();

        if (facultyID.isEmpty()){
            setError(errorLabel1, "Faculty ID cannot be empty");
        }
        if (majorID.isEmpty()){
            setError(errorLabel2, "Major ID cannot be empty");
        }
        if (majorNAME.isEmpty()){
            setError(errorLabel3, "Major Name cannot be empty");
        }

        boolean foundFaculty = false;
        boolean isUpdate = false;

        for (Major major : majorList.getMajors()) {
            if (major.getFacultyId().equalsIgnoreCase(facultyID)) {
                foundFaculty = true;

                if (major.getMajorId().equalsIgnoreCase(majorID)) {
                    setError(errorLabel4, "Major ID already exists");
                    break;
                } else if (major.getMajorName().equalsIgnoreCase(majorNAME)) {
                    setError(errorLabel4, "Major Name already exists");
                    break;
                } else {
                    majorList.addNewMajor(facultyID, majorID, majorNAME);
                    isUpdate = true;
                    break;
                }
            }
        }

// ถ้าไม่มี facultyID ใน list ให้เพิ่ม faculty ใหม่พร้อมกับ major
        if (!foundFaculty) {
            majorList.addNewMajor(facultyID, majorID, majorNAME);
            isUpdate = true;
        }


        if (isUpdate) {
            datasource.writeData(majorList);
            showAlert(Alert.AlertType.INFORMATION, "Major added successfully");
            clearErrorLabels();
            clearTextFiled();
        }

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
    }

    private void clearTextFiled(){
        facultyId.clear();
        majorId.clear();
        majorName.clear();
    }
    public void showAlert(Alert.AlertType alertType, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle("Add New Major");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void backButtonClicked() throws IOException {
        try {
            FXRouter.goTo("faculty-data-admin",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
