package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.StudentList;
import ku.cs.models.User;
import ku.cs.models.Student;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentListFileDatasource;

import java.io.File;
import java.io.IOException;
public class StudentController implements Sidebar {
    @FXML
    private Label usernameLabel;

    private User user;

    private Student student;

    @FXML Label errorLabel;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar
    @FXML
    private Circle imagecircle;


    @FXML
    private void initialize() {
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;
            updateUI();
        }
        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircle.setFill(new ImagePattern(new Image(url)));
        Datasource<StudentList> studentDatasource = new StudentListFileDatasource("data", "student-info.csv");
        StudentList studentList = studentDatasource.readData();
        student = studentList.findStudentById(user.getId());

        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});
    }

    private void updateUI() {
        if (user != null) {
            usernameLabel.setText(user.getUsername());
        }
    }


    // Method สำหรับการคลิกปุ่มเพื่อไปยังหน้าการยื่นคำร้องของนักเรียน
    @FXML
    public void selectAppealButtonClick() {
        if(student.getAdvisorID() == null || student.getAdvisorID().equals("\"\"")) {
            errorLabel.setVisible(true);
        }else{
            navigateTo("student-appeal", user);
        }
    }

    // Method สำหรับการคลิกปุ่มเพื่อไปยังหน้าการติดตามคำร้อง
    @FXML
    public void onAppealTrackingClick() {
        try{
            FXRouter.goTo("appeal-tracking", user);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onPictureClick() {
        navigateTo("user-profile", user);
    }

    private void navigateTo(String route, Object data) {

        try {
            FXRouter.goTo(route, data); // ส่งข้อมูลไปยัง route ที่กำหนด
        } catch (IOException e) {
            System.err.println("Navigation to " + route + " failed: " + e.getMessage());
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
