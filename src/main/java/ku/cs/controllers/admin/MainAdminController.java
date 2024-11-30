package ku.cs.controllers.admin;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.UserListFileDatasource;
import ku.cs.services.FXRouter;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import javafx.scene.layout.Pane;

public class MainAdminController implements Sidebar {
    @FXML
    private TableView<User> tableView;
    @FXML
    private TextField searchUserTextfield;
    @FXML
    private Pane roleSelectionPane;
    @FXML
    private CheckBox studentCheckBox;
    @FXML
    private CheckBox facultyStaffCheckBox;
    @FXML
    private CheckBox departmentStaffCheckBox;
    @FXML
    private CheckBox advisorCheckBox;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar
    @FXML
    private Circle imagecircleuser;
    private User user;


    private UserList userList;

    @FXML
    public void initialize() {
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;

        }
        Datasource<UserList> datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();
        showTable(userList);

        searchUserTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(newValue);
        });
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                User selectedUser = tableView.getSelectionModel().getSelectedItem();
            } else if (event.getClickCount() == 2) {
                User selectedUser = tableView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    try {
                        Pair<User, User> userPair = new Pair<>(user, selectedUser);
                        FXRouter.goTo("user-details", userPair);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        roleSelectionPane.setVisible(false);
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});
        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircleuser.setFill(new ImagePattern(new Image(url)));
    }

    private void filterTable(String searchText) {
        tableView.getItems().clear();

        for (User user : userList.getUsers()) {
            boolean matchesRole = false;

            if (studentCheckBox.isSelected() && user.getRole().equals("student")) {
                matchesRole = true;
            }
            if (facultyStaffCheckBox.isSelected() && user.getRole().equals("facultyStaff")) {
                matchesRole = true;
            }
            if (departmentStaffCheckBox.isSelected() && user.getRole().equals("majorStaff")) {
                matchesRole = true;
            }
            if (advisorCheckBox.isSelected() && user.getRole().equals("advisor")) {
                matchesRole = true;
            }

            if (!user.getRole().equals("admin") &&
                    (matchesRole || (!studentCheckBox.isSelected() && !facultyStaffCheckBox.isSelected() &&
                            !departmentStaffCheckBox.isSelected() && !advisorCheckBox.isSelected())) &&
                    (user.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                            user.getUsername().toLowerCase().contains(searchText.toLowerCase()))) {
                tableView.getItems().add(user);
            }
        }
    }

    private void showTable(UserList userList) {
        tableView.getItems().clear();

        userList.sortUsersByLastLogin();

        // สร้างคอลัมน์
        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(155);

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setPrefWidth(155);

        TableColumn<User, LocalDate> dateColumn = new TableColumn<>("Last Login Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("lastLoginDate"));
        dateColumn.setPrefWidth(155);

        TableColumn<User, LocalTime> timeColumn = new TableColumn<>("Last Login Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("lastLoginTime"));
        timeColumn.setPrefWidth(155);

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleColumn.setPrefWidth(125);

        TableColumn<User, Boolean> suspendColumn = new TableColumn<>("Suspend");
        suspendColumn.setCellValueFactory(new PropertyValueFactory<>("suspended"));
        suspendColumn.setPrefWidth(125);
        suspendColumn.setCellFactory(column -> new TableCell<User, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item ? "Suspended" : "Active");
                }
            }
        });
        TableColumn<User, ImageView> profilePictureColumn = new TableColumn<>("Profile Picture");
        profilePictureColumn.setCellValueFactory(cellData -> {
            String imagePath = System.getProperty("user.dir") + File.separator + cellData.getValue().getProfilePicturePath();
            String url = new File(imagePath).toURI().toString();
            ImageView imageView = new ImageView(new Image(url));
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            return new SimpleObjectProperty<>(imageView);

        });
        profilePictureColumn.setPrefWidth(100);

        tableView.getColumns().clear();
        tableView.getColumns().addAll(profilePictureColumn,nameColumn, usernameColumn, dateColumn, timeColumn, roleColumn, suspendColumn);

        for (User user : userList.getUsers()) {
            if (!user.getRole().equals("admin")) {
                tableView.getItems().add(user);
            }
        }
    }
    @FXML
    private void RoleSelectedButtonClick() {
        roleSelectionPane.setVisible(!roleSelectionPane.isVisible());
    }

    @FXML
    private void enterselectedRoleButtonClick() {
        filterTable(searchUserTextfield.getText());
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
    public void manageStaffDataButtonClick() {
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