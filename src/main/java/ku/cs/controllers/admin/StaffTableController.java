package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.UserListFileDatasource;
import ku.cs.services.FXRouter;

import java.io.File;
import java.io.IOException;


public class StaffTableController implements Sidebar {

    @FXML
    private TableView<User> tableView;
    @FXML
    private TextField searchUserTextfield;
    @FXML
    private CheckBox facultyStaffCheckBox;
    @FXML
    private CheckBox departmentStaffCheckBox;
    @FXML
    private CheckBox advisorCheckBox;
    @FXML
    private Pane roleSelectionPane;
    @FXML
    private Circle imagecircleuser;
    private User user;
    private UserList userList;

    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar

    @FXML
    public void initialize() {
        Datasource<UserList> datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();
        showTable(userList);
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;

        }

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
                        FXRouter.goTo("staff-edit", userPair);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        roleSelectionPane.setVisible(false);
        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircleuser.setFill(new ImagePattern(new Image(url)));
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});

    }

    private void filterTable(String searchText) {
        tableView.getItems().clear();

        for (User user : userList.getUsers()) {
            boolean matchesRole = false;

            if (!facultyStaffCheckBox.isSelected() && !departmentStaffCheckBox.isSelected() && !advisorCheckBox.isSelected()) {
                if (user.getRole().equals("facultyStaff") || user.getRole().equals("majorStaff") || user.getRole().equals("advisor")) {
                    matchesRole = true;
                }
            } else {
                if (facultyStaffCheckBox.isSelected() && user.getRole().equals("facultyStaff")) {
                    matchesRole = true;
                }
                if (departmentStaffCheckBox.isSelected() && user.getRole().equals("majorStaff")) {
                    matchesRole = true;
                }
                if (advisorCheckBox.isSelected() && user.getRole().equals("advisor")) {
                    matchesRole = true;
                }
            }

            if (matchesRole &&
                    (user.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                            user.getUsername().toLowerCase().contains(searchText.toLowerCase()))) {
                tableView.getItems().add(user);
            }
        }
    }



    private void showTable(UserList userList) {
        tableView.getItems().clear(); // เคลียร์ตารางก่อน

        userList.sortUsersByLastLogin();

        // สร้างคอลัมน์
        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(155);

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setPrefWidth(155);

        // เปลี่ยน LocalDate และ LocalTime เป็น Faculty และ Department
        TableColumn<User, String> facultyColumn = new TableColumn<>("Faculty");
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        facultyColumn.setPrefWidth(155);

        TableColumn<User, String> majorColumn = new TableColumn<>("Major");
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));
        majorColumn.setPrefWidth(155);

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

        tableView.getColumns().clear();
        tableView.getColumns().addAll(nameColumn, usernameColumn, facultyColumn, majorColumn, roleColumn, suspendColumn);

        for (User user : userList.getUsers()) {
            if (user.getRole().equals("facultyStaff") || user.getRole().equals("majorStaff") || user.getRole().equals("advisor")) {
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
    public void addStaffButtonClick() {
        try {
            FXRouter.goTo("add-staff",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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




