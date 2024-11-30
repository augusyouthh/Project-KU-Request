package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.Student;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.File;
import java.io.IOException;

public class UserDetailAdminController implements Sidebar {
    @FXML
    private Label suspendlabel;
    @FXML
    private Label userTextLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label facultyLabel;
    @FXML
    private Label DepartmentLabel;

    @FXML
    private Circle imagecircleuser;
    @FXML
    private Circle imagecircle;

    private UserList userList;
    private UserListFileDatasource datasource;
    private User user;
    private User userdetail;

    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar

    public void initialize() {
        Object data = FXRouter.getData();
        if (data instanceof Pair) {
            Pair<User, User> userPair = (Pair<User, User>) data;
            user = userPair.getKey();
            userdetail = userPair.getValue();
        }
        suspendlabel.setText("");
        displayUserInfo();
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});
        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircleuser.setFill(new ImagePattern(new Image(url)));
    }


    private void displayUserInfo() {
        nameLabel.setText("ชื่อ: " + userdetail.getName());
        usernameLabel.setText("ชื่อผู้ใช้: " + userdetail.getUsername());
        facultyLabel.setText("คณะ: " + userdetail.getFaculty());


        if (userdetail.getRole().equals("student")) {
            userTextLabel.setText("ข้อมูลนิสิต");
        } else if (userdetail.getRole().equals("advisor")) {
            userTextLabel.setText("ข้อมูลอาจารย์ที่ปรึกษา");
        } else if (userdetail.getRole().equals("facultyStaff")) {
            userTextLabel.setText("ข้อมูลเจ้าหน้าที่คณะ");
        } else if (userdetail.getRole().equals("majorStaff")) {
            userTextLabel.setText("ข้อมูลเจ้าหน้าที่ภาควิชา");
        }

        if (userdetail.getRole().equals("facultyStaff")) {
            DepartmentLabel.setText("");
        } else {
            DepartmentLabel.setText("ภาควิชา: " +userdetail.getMajor());
        }
        String imagePath = System.getProperty("user.dir") + File.separator + userdetail.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircle.setFill(new ImagePattern(new Image(url)));
    }

    @FXML
    public void suspendButtonclick() {
        datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();
        String username = userdetail.getUsername();

        User user = userList.findUserByUsername(username);
        if (user != null) {
            user.setSuspended(true);
            datasource.writeData(userList);
            suspendlabel.setText("ผู้ใช้ " + username + " ถูกระงับเรียบร้อยแล้ว.");
        }
    }
    @FXML
    public void unSuspendButtonclick() {
        datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();
        String username = userdetail.getUsername();

        User user = userList.findUserByUsername(username);
        if (user != null) {
            user.setSuspended(false);
            datasource.writeData(userList);
            suspendlabel.setText("ผู้ใช้ " + username + " ถูกปลดระงับเรียบร้อยแล้ว.");

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
