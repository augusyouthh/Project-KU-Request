package ku.cs.controllers.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ku.cs.controllers.MyteamController;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class SidebarController{
    private Sidebar sidebar;
    @FXML Button backButton;
    @FXML Button settingsButton;
    @FXML AnchorPane mainPane;
    @FXML private AnchorPane settingsPane;
    @FXML private Button myTeamButton;

    public void setSidebar(Sidebar sidebar) {
        this.sidebar = sidebar;
    }

    @FXML
    public void initialize() {
        backButton.setOnAction(actionEvent -> {closeSidebar();});
//        settingsButton.setOnAction(actionEvent -> {toggleSetting();});
    }
    public void closeSidebar() {
        if (sidebar != null) {
            sidebar.closeSidebar(); // เรียกใช้ฟังก์ชัน closeSidebar() ของ Sidebar ที่ถูกตั้งค่า
        }
    }
    @FXML
    public void onLogoutButtonClick() {
        try{
            FXRouter.goTo("login-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void onMyTeamButtonClick() {
        try {
            // เก็บ Scene ปัจจุบันเพื่อใช้ย้อนกลับ
            Stage stage = (Stage) myTeamButton.getScene().getWindow();
            Scene currentScene = stage.getScene();

            // โหลดหน้าต่าง My Team
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/other/my-team.fxml"));
            Parent root = loader.load();

            // ตั้งค่า Scene ก่อนหน้าให้ MyteamController เพื่อใช้ในการย้อนกลับ
            MyteamController controller = loader.getController();  // แก้เป็น MyteamController
            controller.setPreviousScene(currentScene);  // ต้องมีการตั้ง method setPreviousScene ใน MyteamController

            // เปลี่ยน Scene ไปหน้า My Team
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
