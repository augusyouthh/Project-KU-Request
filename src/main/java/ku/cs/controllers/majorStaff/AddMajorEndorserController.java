package ku.cs.controllers.majorStaff;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.MajorEndorserList;
import ku.cs.models.User;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.MajorEndorserListFileDatasource;

import java.io.File;
import java.io.IOException;

public class AddMajorEndorserController implements Sidebar {
    @FXML private Label nameLabel;
    @FXML private Button confirmButton;
    @FXML private Label errorLabel1;
    @FXML private Label errorLabel2;
    @FXML private Label errorLabel3;
    @FXML private TextField nameId, positionId;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar

    private User user;
    private Datasource<MajorEndorserList> datasource;
    private MajorEndorserList majorEndorserList;
    @FXML private Circle imagecircle;


    @FXML
    public void initialize() {
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;

        } else {
            nameLabel.setText("Invalid user data");
        }

        clearErrorLabels();

        // Initialize the datasource and read data for MajorEndorserList
        datasource = new MajorEndorserListFileDatasource("data", "major-endorser.csv");
        majorEndorserList = datasource.readData();

        confirmButton.setOnAction(actionEvent -> addMajorEndorser());
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});
        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircle.setFill(new ImagePattern(new Image(url)));
    }

    private void addMajorEndorser() {
        String name = nameId.getText().trim();
        String position = positionId.getText().trim() + user.getMajor() + user.getFaculty();

        if (isInputValid(name, position)) {
            majorEndorserList.addNewMajorEndorser(name, position); // ใช้ค่าที่ได้จากการป้อนข้อมูล
            datasource.writeData(majorEndorserList); // Corrected datasource writing
            showAlert(Alert.AlertType.INFORMATION, "Success", "Major endorser added successfully");
            clearErrorLabels();
            clearTextFields();
        }
    }

    private boolean isInputValid(String name, String position) {
        if (name.isEmpty()) {
            setError(errorLabel1, "Please enter name");
            return false;
        }
        if (position.isEmpty()) {
            setError(errorLabel2, "Please enter position");
            return false;
        }
        // Checking for duplicate name in the current list of major endorsers
        if (majorEndorserList.getMajorEndorsers().stream().anyMatch(e -> e.getName().equals(name))) {
            setError(errorLabel3, "This major endorser is already in the database");
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

    private void clearTextFields() {
        nameId.clear();
        positionId.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void onHomeButtonClick() {
        navigateTo("departmentStaff", user);
    }

    @FXML
    public void onStudentListButton(){
        navigateTo("student-in-major", user);
    }

    @FXML
    public void onEditMajorEndorserButton(){
        navigateTo("edit-major-endorser", user);
    }

    @FXML
    public void onAddEndorserButton(){
        navigateTo("add-major-endorser", user);
    }

    @FXML
    public void onApproveMajorButton(){
        navigateTo("approve-major-list", user);
    }
    @FXML
    public void onUserProfileButton(){
        navigateTo("user-profile", user);
    }


    private void navigateTo(String route, Object data) {
        try {
            FXRouter.goTo(route, data);
        } catch (IOException e) {
            System.err.println("Navigation to " + route + " failed: " + e.getMessage());
        }
    }

    @Override
    public void loadSidebar(){
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
