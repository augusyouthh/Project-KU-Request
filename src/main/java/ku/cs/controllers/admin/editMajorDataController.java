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


public class editMajorDataController {
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
    private Datasource<MajorList> datasource;
    private MajorList majorList;
    @FXML private Button okButton;
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
        okButton.setOnAction(actionEvent -> {onOkButtonClicked();});
    }

    public void onOkButtonClicked() {
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

        boolean isUpdated = false;
        for (Major major : majorList.getMajors()){
           if (major.getFacultyId().equalsIgnoreCase(facultyID)){
               if (major.getMajorId().equalsIgnoreCase(majorID) && major.getMajorName().equalsIgnoreCase(majorNAME)){
                   setError(errorLabel4, "Major already exists");
               }
               else if (major.getMajorId().equalsIgnoreCase(majorID)){
                   major.setMajorName(majorNAME);
                   isUpdated = true;
               } else if (major.getMajorName().equalsIgnoreCase(majorNAME)) {
                   major.setMajorId(majorID);
                   isUpdated = true;
               }
               break;
           }
        }
        if (isUpdated){
            datasource.writeData(majorList);
            showAlert(Alert.AlertType.INFORMATION, "Major updated successfully");
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
    }

    private void clearTextFiled(){
        facultyId.clear();
        majorId.clear();
        majorName.clear();
    }
    private void showAlert(Alert.AlertType alertType, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void onBackButtonClicked() {
        try{
            FXRouter.goTo("faculty-data-admin",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onAddMajorButtonClicked() {
        try{
            FXRouter.goTo("add-new-major",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
