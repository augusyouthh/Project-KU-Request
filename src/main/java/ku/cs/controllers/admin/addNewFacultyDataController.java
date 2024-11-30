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
import ku.cs.models.FacultyList;

import java.io.IOException;

public class addNewFacultyDataController {
    @FXML private Label errorLabel1;
    @FXML private Label errorLabel2;
    @FXML private Label errorLabel3;
    @FXML
    private TextField facultyId;

    @FXML
    private TextField facultyName;
    @FXML private Button addButton;

    private Datasource<FacultyList> datasource;
    private FacultyList facultyList;

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
        addButton.setOnAction(actionEvent -> {addNewFacultyButtonClicked();});
    }

    public void addNewFacultyButtonClicked(){
        String id = facultyId.getText().trim();
        String name = facultyName.getText().trim();

        if (id.isEmpty()) {
            setError(errorLabel1, "Faculty ID cannot be empty");
            return;
        }

        if (name.isEmpty()) {
            setError(errorLabel2, "Faculty name cannot be empty");
            return;
        }

        for (Faculty faculty : facultyList.getFaculties()) {
            if (faculty.getFacultyId().equals(id)) {
                showError("Faculty already had faculty id");
                return;
            } else if (faculty.getFacultyName().equals(name)) {
                showError("Faculty already had faculty name");
                return;
            }
        }

        facultyList.addNewFaculty(id, name);
        datasource.writeData(facultyList);
        showAlert(Alert.AlertType.INFORMATION, "Success!", "Faculty has been added successfully!");
        clearErrorLabels();
        clearTextFiled();
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
        facultyName.clear();
        facultyId.clear();
    }

    public void showAlert(Alert.AlertType alertType, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showError(String message){
        errorLabel3.setText(message);
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