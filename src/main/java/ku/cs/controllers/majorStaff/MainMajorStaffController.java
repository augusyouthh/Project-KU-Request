package ku.cs.controllers.majorStaff;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.controllers.components.AppealItemController;
import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;


public class MainMajorStaffController implements Sidebar {

    @FXML
    private VBox appealVBox;

    @FXML
    private Label noAppealsLabel;

    @FXML
    private TextField searchTextField;
    @FXML private Label nameLabel;
    private User user;
    private AppealList appealList;
    private AppealListDatasource datasource;
    private AppealList appealListInMajor;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar
    @FXML private Circle imagecircle;

    @FXML
    private void initialize() {
        Object data = FXRouter.getData();

        datasource = new AppealListDatasource("data/appeals.csv");
        appealList = datasource.readData();
        AppealSharedData.setNormalAppealList(appealList);
        if (data instanceof User) {
            user = (User) data;
            updateUI();
        } else {
            nameLabel.setText("Invalid user data");
        }
        Datasource<StudentList> studentDatasource = new StudentListFileDatasource("data", "student-info.csv");
        StudentList studentList = studentDatasource.readData();
        StudentList studentsListInMajor = studentList.getStudentsListBYMajor(user.getMajor());
        appealListInMajor = appealList.findAppealByStudentID(studentsListInMajor);
        loadAppeals(null, null);
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});

        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircle.setFill(new ImagePattern(new Image(url)));

    }

    private void loadAppeals(String filterType, String searchQuery) {
        // Fetch the list of appeals in the major
        List<Appeal> appeals = appealListInMajor.getsAppeals();

        appeals = appeals.stream()
                .filter(appeal -> appeal.getStatus().trim().equalsIgnoreCase("อนุมัติโดยอาจารย์ที่ปรึกษา คำร้องส่งต่อให้หัวหน้าภาควิชา") ||
                        appeal.getStatus().contains("หัวหน้าภาควิชา"))
                .collect(Collectors.toList());

        if (filterType != null && !filterType.isEmpty()) {
            appeals = appeals.stream()
                    .filter(appeal -> appeal.getType().equals(filterType))
                    .collect(Collectors.toList());
        }

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            String lowerCaseQuery = searchQuery.trim().toLowerCase();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            appeals = appeals.stream()
                    .filter(appeal ->
                            appeal.getSubject().toLowerCase().contains(lowerCaseQuery) || // Search by subject
                                    appeal.getRequest().toLowerCase().contains(lowerCaseQuery) || // Search by request content
                                    appeal.getDate().format(dateFormatter).contains(lowerCaseQuery) || // Search by date
                                    appeal.getStudentSignature().toLowerCase().contains(lowerCaseQuery) // Search by student signature
                    ).collect(Collectors.toList());
        }

        // Sort appeals using the comparator
        appeals.sort(new AppealSortComparator(filterType));


        if (appeals.isEmpty()) {
            noAppealsLabel.setVisible(true); // Show "no appeals" message if list is empty
        } else {
            // Update the UI based on the filtered appeals
            appealVBox.getChildren().clear(); // Clear previous list
            noAppealsLabel.setVisible(false); // Hide "no appeals" message if appeals are present
            // Load each filtered appeal into the VBox
            appeals.forEach(appeal -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/student/appeal-item.fxml"));
                    Pane pane = loader.load();

                    AppealItemController controller = loader.getController();
                    controller.setAppealData(appeal); // Set appeal data for the view

                    appealVBox.getChildren().add(pane); // Add pane to VBox
                } catch (IOException e) {
                    e.printStackTrace(); // Log error in case of failure
                }
            });
        }
    }

    @FXML   //สำหรับ search หาคำร้อง
    public void onSearchButtonClick() {
        String searchQuery = searchTextField.getText();
        loadAppeals(null, searchQuery);
    }

    @FXML   //กรองเเค่คำร้องทั่วไป
    public void showNormalAppealsOnly() {
        loadAppeals("คำร้องทั่วไป:", null);
    }

    @FXML   //กรองเเค่ใบลาพักการศึกษา
    public void showLeaveAppealsOnly() {
        loadAppeals("ใบลาพักการศึกษา:", null);
    }

    @FXML   //กรองเเค่คำร้องขอลงทะเบียนเรียน
    public void showEnrollAppealsOnly() {
        loadAppeals("ขอลงทะเบียนเรียน:", null);
    }
    @FXML   //เห็นคำร้องทุกประเภท
    public void showAllAppeals() {
        loadAppeals(null, null);
    }

    private void updateUI() {
        if (user != null) {
            nameLabel.setText(user.getUsername());
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

    //Navbar button
    @FXML
    public void onApproveMajorButton(){
        navigateTo("approve-major-list", user);
    }

    @FXML
    public void onStudentListButton(){
        navigateTo("student-in-major", user);
    }

    @FXML
    public void onAddEndorserButton(){
        navigateTo("add-major-endorser", user);
    }

    @FXML
    public void onHomeButton(){
        navigateTo("departmentStaff", user);
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
}