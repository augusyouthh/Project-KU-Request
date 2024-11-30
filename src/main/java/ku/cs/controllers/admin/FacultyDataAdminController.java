package ku.cs.controllers.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import ku.cs.models.Faculty;
import ku.cs.models.User;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.FacultyListFileDatasource;
import ku.cs.models.FacultyList;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class FacultyDataAdminController implements Sidebar {
    @FXML private TableView<Faculty> facultyDataAdminTableView;

    @FXML
    private Circle imagecircleuser;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar

    private User user;
    private FacultyList facultyList;
    private Datasource<FacultyList> datasource;
    private String selectedFaculty;

    @FXML
    public void initialize() {
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;

        }
        datasource = new FacultyListFileDatasource("data", "Faculty.csv");
        facultyList = datasource.readData();
        if (facultyList != null){
            showTable(facultyList);
        }
        else {
            System.out.println("Failed to load faculty list.");
        }

        facultyDataAdminTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Faculty>() {
            @Override
            public void changed(ObservableValue observableValue, Faculty oldValue, Faculty newValue) {
                if (newValue != null) {
                    selectedFaculty = newValue.getFacultyId();
                    try{
                        FXRouter.goTo("major-data-admin", new Pair<>(user, selectedFaculty));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});
        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircleuser.setFill(new ImagePattern(new Image(url)));
    }


    private void showTable(FacultyList facultyList){
        // กำหนด column ให้มี title ว่า ID และใช้ค่าจาก attribute id ของ object Student
        TableColumn<Faculty, String> idColumn = new TableColumn<>("Faculty ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("facultyId"));//เรียกมาจาก getter

        // กำหนด column ให้มี title ว่า Name และใช้ค่าจาก attribute name ของ object Student
        TableColumn<Faculty, String> nameColumn = new TableColumn<>("Faculty Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("facultyName"));

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        facultyDataAdminTableView.getColumns().clear();
        facultyDataAdminTableView.getColumns().add(idColumn);
        facultyDataAdminTableView.getColumns().add(nameColumn);
        facultyDataAdminTableView.getItems().clear();

        // ใส่ข้อมูล Student ทั้งหมดจาก studentList ไปแสดงใน TableView
        for (Faculty faculty: facultyList.getFaculties()) {
            facultyDataAdminTableView.getItems().add(faculty);
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
    @FXML
    public void onHomeButtonClick() {
        try {
            FXRouter.goTo("main-admin",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @FXML
    public void onEditFacultyButtonClick(){
        try {
            FXRouter.goTo("edit-data-faculty",user);
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