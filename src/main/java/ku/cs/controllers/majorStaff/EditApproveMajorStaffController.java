package ku.cs.controllers.majorStaff;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.ApproveFacultyStaff;
import ku.cs.models.MajorEndorser;
import ku.cs.models.MajorEndorserList;
import ku.cs.models.User;
import ku.cs.services.ApproveFacultyStaffListDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.MajorEndorserListFileDatasource;

import java.io.IOException;

public class EditApproveMajorStaffController {
    @FXML
    private Label nameLabel;
    @FXML private Button confirmButton;
    @FXML private Label errorLabel1;
    @FXML private Label errorLabel2;
    @FXML private Label errorLabel3;
    @FXML private TextField nameId, positionId;

    private User user;
    private Datasource<MajorEndorserList> datasource;
    private MajorEndorserList majorEndorserList;

    @FXML
    public void initialize() {
        errorLabel1.setText("");
        errorLabel2.setText("");
        errorLabel3.setText("");
        datasource = new MajorEndorserListFileDatasource("data", "major-endorser.csv");
        majorEndorserList = datasource.readData();
        confirmButton.setOnAction(actionEvent -> {ConfirmButton();});
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;
        }
    }

    private void ConfirmButton() {
        String name = nameId.getText().trim();
        String position = positionId.getText().trim();


        if (!isInputValid(name, position)) return;

        if (updateFacultyStaff(name, position)) {
            datasource.writeData(majorEndorserList);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Data has been updated");
            clearErrorLabels();
        }
    }

    private boolean updateFacultyStaff(String name, String position) {
        for (MajorEndorser majorEndorser : majorEndorserList.getMajorEndorsers()) {
            if (majorEndorser.getName().equals(name)) {
                if (majorEndorser.getPosition().equals(position)) {
                    setError(errorLabel3, "Data is already in the database");
                    return false;
                }
                majorEndorser.setPosition(position);
                return true;
            }
        }
        setError(errorLabel3, "No matching major endorser found");
        return false;
    }

    private boolean isInputValid(String name, String position) {
        if (name.isEmpty()) {
            setError(errorLabel1, "Please enter major endorser's name");
            return false;
        }
        if (position.isEmpty()) {
            setError(errorLabel2, "Please enter major endorser's position");
            return false;
        }

        return true;
    }

    private void setError(Label label, String message) {
        clearErrorLabels(); // Clear all error labels before setting the new one
        label.setText(message);
    }

    private void clearErrorLabels() {
        errorLabel1.setText("");
        errorLabel2.setText("");
        errorLabel3.setText("");

    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateUI() {
        if (user != null) {
            nameLabel.setText(user.getUsername());
        }
    }

    @FXML
    public void onBackButton() {
        navigateTo("add-major-endorser", user);
    }


    private void navigateTo(String route, Object data) {
        try {
            FXRouter.goTo(route, data);
        } catch (IOException e) {
            System.err.println("Navigation to " + route + " failed: " + e.getMessage());
        }
    }
}
