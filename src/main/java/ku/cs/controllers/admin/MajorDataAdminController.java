package ku.cs.controllers.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.Faculty;
import ku.cs.models.Major;
import ku.cs.models.MajorList;
import ku.cs.models.User;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.MajorListFileDatasource;

import java.io.File;
import java.io.IOException;

public class MajorDataAdminController implements Sidebar {
    @FXML private TableView<Major> majorDataAdminTableView;

    private MajorList majorList;

    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar
    @FXML
    private Circle imagecircleuser;

    private User user;

    private String selectedFaculty;

    @FXML
    public void initialize(){
        Object data = FXRouter.getData();
        if (data instanceof Pair) {
            Pair<User, String> pair = (Pair<User, String>) data;
            user = pair.getKey();
            selectedFaculty = pair.getValue();

        }
        Datasource<MajorList> datasource = new MajorListFileDatasource("data", "Major.csv");
        majorList = datasource.readData();
        if (majorList != null){
            filterByFacultyId(selectedFaculty); // กรองข้อมูลด้วย facultyId
        }
        else {
            System.out.println("Failed to load major list.");
        }

        majorDataAdminTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Major>() {
            @Override
            public void changed(ObservableValue observableValue, Major oldValue, Major newValue) {
                if (newValue != null);
            }
        });
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});
        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircleuser.setFill(new ImagePattern(new Image(url)));
    }

    private void filterByFacultyId(String facultyId) {
        MajorList filteredList = new MajorList();
        for (Major major : majorList.getMajors()) {
            if (major.getFacultyId().equals(facultyId)) {
                filteredList.addNewMajor(major.getFacultyId(), major.getMajorId(), major.getMajorName());
            }
        }
        showTable(filteredList);
    }

    private void showTable(MajorList majorList){
        TableColumn<Major, String> facultyIDCol = new TableColumn<>("Faculty ID");
        facultyIDCol.setCellValueFactory(new PropertyValueFactory<>("facultyId"));

        TableColumn<Major, String> majorIdCol = new TableColumn<>("Major ID");
        majorIdCol.setCellValueFactory(new PropertyValueFactory<>("majorId"));

        TableColumn<Major, String> majorNameCol = new TableColumn<>("Major Name");
        majorNameCol.setCellValueFactory(new PropertyValueFactory<>("majorName"));


        majorDataAdminTableView.getColumns().clear();
        majorDataAdminTableView.getColumns().add(facultyIDCol);
        majorDataAdminTableView.getColumns().add(majorIdCol);
        majorDataAdminTableView.getColumns().add(majorNameCol);
        majorDataAdminTableView.getItems().clear();

        for (Major major : majorList.getMajors()){
            majorDataAdminTableView.getItems().add(major);
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
    public void onHomeButtonClick() {
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
    public void onBackButtonClick() {
        try{
            FXRouter.goTo("faculty-data-admin",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onAddNewMajorButtonClick() {
        try{
            FXRouter.goTo("edit-data-major",user);
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
