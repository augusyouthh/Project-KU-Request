package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.Faculty;
import ku.cs.models.FacultyList;
import ku.cs.models.User;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.FacultyListFileDatasource;

import java.io.*;

public class editDataFacultyController {
    @FXML private TextField facultyId;
    @FXML private TextField facultyName;
    @FXML private Button okButton;
    private Datasource<FacultyList> datasource;
    private FacultyList facultyList;
    @FXML private Label errorLabel1;
    @FXML private Label errorLabel2;
    @FXML private Label errorLabel3;

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
        datasource = new FacultyListFileDatasource("data", "Faculty.csv");
        facultyList = datasource.readData();
        okButton.setOnAction(actionEvent -> {
            try {
                okButtonClicked();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void okButtonClicked() throws IOException {
        String Id = facultyId.getText().trim();
        String Name = facultyName.getText().trim();
        if (Id.isEmpty()){
            setError(errorLabel1, "Faculty ID cannot be empty");
        }
        else if (Name.isEmpty()){
            setError(errorLabel2, "Faculty Name cannot be empty");
        }

        boolean isUpdated = false;
        for (Faculty faculty : facultyList.getFaculties()){
            if (faculty.getFacultyId().equals(Id)){
                if (faculty.getFacultyName().equals(Name)){
                    setError(errorLabel3, "Data faculty already exists");
                }
                faculty.setFacultyName(Name);
                isUpdated = true;
                break;
            } else if (faculty.getFacultyName().equals(Name)) {
                faculty.setFacultyId(Id);
                isUpdated = true;
                break;
            }
        }
        if (isUpdated){
            datasource.writeData(facultyList);
            showAlert(Alert.AlertType.INFORMATION, "Success!", "Faculty updated successfully!");
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
        facultyName.clear();
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
            FXRouter.goTo("faculty-data-admin",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onAddNewFacultyButtonClicked(){
        try {
            FXRouter.goTo("add-new-faculty",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}