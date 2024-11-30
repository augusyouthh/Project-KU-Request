package ku.cs.controllers.facultyStaff;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.ApproveFacultyStaff;
import ku.cs.models.ApproveFacultyStaffList;
import ku.cs.models.User;
import ku.cs.services.ApproveFacultyStaffListDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class ApproverFacultyStaffController implements Sidebar {
    @FXML
    private TableView<ApproveFacultyStaff> approveFacultyStaffTableView;

    private ApproveFacultyStaffList approveFacultyStaffList;
    private Datasource<ApproveFacultyStaffList> datasource;

    private User user;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar



    @FXML
    public void initialize() {
        datasource = new ApproveFacultyStaffListDatasource("data", "approveFacultyStaff.csv");
        approveFacultyStaffList = datasource.readData();
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;
            showTable(approveFacultyStaffList);
        }
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});
    }
    private void showTable(ApproveFacultyStaffList approveFacultyStaffList) {

        TableColumn<ApproveFacultyStaff, String> nameColumn = new TableColumn<>("ชื่อ");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ApproveFacultyStaff, String> roleColumn = new TableColumn<>("ตำแหน่ง");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<ApproveFacultyStaff, String> facultyColumn = new TableColumn<>("คณะ");
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("faculty"));

        approveFacultyStaffTableView.getColumns().clear();
        approveFacultyStaffTableView.getColumns().add(nameColumn);
        approveFacultyStaffTableView.getColumns().add(roleColumn);
        approveFacultyStaffTableView.getColumns().add(facultyColumn);

        approveFacultyStaffTableView.getItems().clear();

        for (ApproveFacultyStaff approveFacultyStaff: approveFacultyStaffList.getApproveFacultyStaffList()) {
            if (approveFacultyStaff.getFaculty().contains(user.getFaculty())){
                approveFacultyStaffTableView.getItems().add(approveFacultyStaff);
            }

        }
    }

    @FXML
    public void onEditApproveFacultyStaff(){
        try {
            FXRouter.goTo("editApproveFacultyStaff");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onHomeButton(){
        try{
            FXRouter.goTo("facultyStaff");
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
