package ku.cs.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.controllers.components.Sidebar;
import ku.cs.controllers.components.SidebarController;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController implements Sidebar {
    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton;
    @FXML
    private ChoiceBox<String> facultyUserChoicebox;
    @FXML
    private ChoiceBox<String> facultyAppealChoiceBox;
    @FXML
    private PieChart UserInFacultyPieChart;
    @FXML
    private PieChart AppealStatusPieChart;
    @FXML
    private PieChart AppealInFacultyPieChart;
    @FXML
    private PieChart UserInMajorPieChart;
    @FXML
    private PieChart AppealInMajorPieChart;

    @FXML
    private Label totalAppealLabel;
    @FXML
    private Label totalUserLabel;
    @FXML
    private Label totalAccecptAppealLabel;
    @FXML
    private Circle imagecircleuser;

    private User user;
    private MajorListFileDatasource majorDatasource;
    private FacultyListFileDatasource facultyDatasource;
    private UserList userList;
    private AppealList appealList;


    @FXML
    public void initialize() {
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;

        }
        UserInMajorPieChart.setVisible(false);
        AppealInMajorPieChart.setVisible(false);
        facultyDatasource = new FacultyListFileDatasource("data", "faculty.csv");
        UserListFileDatasource datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();
        AppealListDatasource appealDatasource = new AppealListDatasource("data/appeals.csv");
        appealList = appealDatasource.readData();
        loadSidebar();// loadSidebar
        toggleSidebarButton.setOnAction(actionEvent -> {toggleSidebar();});

        loadFacultyChoicesForUser();
        loadFacultyChoicesForAppeals();
        updateTotalUserLabel();
        updateTotalAppealLabel();
        updateTotalAcceptedAppealLabel();
        Map<String, Integer> facultyCountMap = countUsersByFaculty();

        setPieChartUserFaculty(facultyCountMap);

        Map<String, Integer> appealStatusCountMap = countAppealsByStatus();
        setAppealStatusPieChartData(appealStatusCountMap);

        Map<String, Integer> facultyAppealCountMap = countAppealsByFaculty();
        setAppealInFacultyPieChartData(facultyAppealCountMap);

        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircleuser.setFill(new ImagePattern(new Image(url)));
    }

    private void updateTotalUserLabel() {
        int totalUsers = userList.getUsers().size();
        totalUserLabel.setText("" + totalUsers);
    }
    private void updateTotalAppealLabel() {
        int totalUsers = appealList.getsAppeals().size();
        totalAppealLabel.setText("" + totalUsers);
    }
    private void updateTotalAcceptedAppealLabel() {
        int totalAcceptedAppeals = 0;

        for (Appeal appeal : appealList.getsAppeals()) {
            if (appeal.getStatus().contains("คำร้องดำเนินการครบถ้วน")) {
                totalAcceptedAppeals++;
            }
        }
        totalAccecptAppealLabel.setText("" + totalAcceptedAppeals);
    }

    private Map<String, Integer> countAppealsByStatus() {
        Map<String, Integer> statusCountMap = new HashMap<>();

        for (Appeal appeal : appealList.getsAppeals()) {
            String status = appeal.getStatus();
            statusCountMap.put(status, statusCountMap.getOrDefault(status, 0) + 1);
        }

        return statusCountMap;
    }


    private void setAppealStatusPieChartData(Map<String, Integer> appealStatusCountMap) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : appealStatusCountMap.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChartData.add(data);
        }

        AppealStatusPieChart.setData(pieChartData);
        AppealStatusPieChart.setLabelsVisible(false);
        AppealStatusPieChart.setLabelLineLength(0);

        for (PieChart.Data data : AppealStatusPieChart.getData()) {
            data.setName(data.getName() + ": " + (int) data.getPieValue() + " คำร้อง");
        }
    }

    private Map<String, Integer> countAppealsByFaculty() {
        Map<String, Integer> facultyAppealCountMap = new HashMap<>();

        for (Appeal appeal : appealList.getsAppeals()) {
            if (appeal.getStatus().contains("คำร้องดำเนินการครบถ้วน")) {
                String studentID = appeal.getStudentID();

                User user = userList.getUsers().stream()
                        .filter(u -> u.getId().equals(studentID))
                        .findFirst()
                        .orElse(null);

                if (user != null) {
                    String faculty = user.getFaculty();
                    facultyAppealCountMap.put(faculty, facultyAppealCountMap.getOrDefault(faculty, 0) + 1);
                }
            }
        }

        return facultyAppealCountMap;
    }


    private void setAppealInFacultyPieChartData(Map<String, Integer> facultyAppealCountMap) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : facultyAppealCountMap.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChartData.add(data);
        }

        AppealInFacultyPieChart.setData(pieChartData);
        AppealInFacultyPieChart.setLabelsVisible(false);
        AppealInFacultyPieChart.setLabelLineLength(0);

        for (PieChart.Data data : AppealInFacultyPieChart.getData()) {
            data.setName(data.getName() + ": " + (int) data.getPieValue() + " คำร้อง");
        }
    }




    private void setPieChartUserFaculty(Map<String, Integer> facultyCountMap) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : facultyCountMap.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChartData.add(data);
        }

        UserInFacultyPieChart.setData(pieChartData);
        UserInFacultyPieChart.setLabelsVisible(false);
        UserInFacultyPieChart.setLabelLineLength(0);

        for (PieChart.Data data : UserInFacultyPieChart.getData()) {
            data.setName(data.getName() + ": " + (int) data.getPieValue());
        }
    }


    private Map<String, Integer> countUsersByFaculty() {
        Map<String, Integer> facultyCountMap = new HashMap<>();

        for (User user : userList.getUsers()) {
            String faculty = user.getFaculty();
            facultyCountMap.put(faculty, facultyCountMap.getOrDefault(faculty, 0) + 1);
        }

        return facultyCountMap;
    }


    private void loadFacultyChoicesForUser() {
        facultyUserChoicebox.getItems().clear();


        ObservableList<String> facultyNames = FXCollections.observableArrayList();
        for (Faculty faculty : facultyDatasource.readData().getFaculties()) {
            facultyNames.add(faculty.getFacultyName());
        }

        facultyUserChoicebox.getItems().addAll(facultyNames);

        facultyUserChoicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                String selectedFacultyId = facultyDatasource.readData().getFaculties().stream()
                        .filter(faculty -> faculty.getFacultyName().equals(newValue))
                        .findFirst()
                        .map(Faculty::getFacultyId)
                        .orElse(null);

                if (selectedFacultyId != null) {
                    UserInMajorPieChart.setDisable(false);
                    UserInMajorPieChart.setVisible(true);

                    showMajorPieChart(selectedFacultyId);
                }
            } else {

                UserInMajorPieChart.setDisable(true);
                UserInMajorPieChart.setVisible(false);
            }
        });
    }
    private void loadFacultyChoicesForAppeals() {
        facultyAppealChoiceBox.getItems().clear();

        ObservableList<String> facultyNames = FXCollections.observableArrayList();
        for (Faculty faculty : facultyDatasource.readData().getFaculties()) {
            facultyNames.add(faculty.getFacultyName());
        }

        facultyAppealChoiceBox.getItems().addAll(facultyNames);


        facultyAppealChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedFacultyId = facultyDatasource.readData().getFaculties().stream()
                        .filter(faculty -> faculty.getFacultyName().equals(newValue))
                        .findFirst()
                        .map(Faculty::getFacultyId)
                        .orElse(null);

                if (selectedFacultyId != null) {

                    AppealInMajorPieChart.setDisable(false);
                    AppealInMajorPieChart.setVisible(true);
                    showAppealsMajorPieChart(selectedFacultyId);
                }
            } else {

                AppealInMajorPieChart.setDisable(true);
                AppealInMajorPieChart.setVisible(false);
            }
        });
    }


    private void showAppealsMajorPieChart(String facultyId) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Map<String, Integer> majorAppealCountMap = new HashMap<>();

        for (Appeal appeal : appealList.getsAppeals()) {
            if (appeal.getStatus().contains("คำร้องดำเนินการครบถ้วน")) {
                String studentID = appeal.getStudentID();
                User user = null;

                for (User u : userList.getUsers()) {
                    if (u.getId().equals(studentID)) {
                        user = u;
                        break;
                    }
                }

                if (user != null) {
                    String userFacultyId = null;
                    List<Faculty> faculties = facultyDatasource.readData().getFaculties();
                    for (Faculty faculty : faculties) {
                        if (faculty.getFacultyName().equals(user.getFaculty())) {
                            userFacultyId = faculty.getFacultyId();
                            break;
                        }
                    }


                    if (userFacultyId != null && userFacultyId.equals(facultyId)) {
                        String major = user.getMajor() != null ? user.getMajor() : "ไม่สังกัดภาควิชา";
                        majorAppealCountMap.put(major, majorAppealCountMap.getOrDefault(major, 0) + 1);
                    }
                }
            }
        }

        for (Map.Entry<String, Integer> entry : majorAppealCountMap.entrySet()) {
            if (entry.getValue() > 0) {
                PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
                pieChartData.add(data);
            }
        }

        AppealInMajorPieChart.setData(pieChartData);
        AppealInMajorPieChart.setLabelsVisible(false);
        AppealInMajorPieChart.setLabelLineLength(0);

        for (PieChart.Data data : AppealInMajorPieChart.getData()) {
            data.setName(data.getName() + ": " + (int) data.getPieValue() + " คำร้อง");
        }
    }





    private void showMajorPieChart(String selectedFacultyId) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Map<String, Integer> majorUserCountMap = new HashMap<>();


        for (User user : userList.getUsers()) {
            String userFacultyId = null;


            List<Faculty> faculties = facultyDatasource.readData().getFaculties();
            for (Faculty faculty : faculties) {
                if (faculty.getFacultyName().equals(user.getFaculty())) {
                    userFacultyId = faculty.getFacultyId();
                    break;
                }
            }

            if (userFacultyId != null && userFacultyId.equals(selectedFacultyId)) {
                String major = user.getMajor() != null ? user.getMajor() : "ไม่สังกัดภาควิชา";
                majorUserCountMap.put(major, majorUserCountMap.getOrDefault(major, 0) + 1);
            }
        }



        for (Map.Entry<String, Integer> entry : majorUserCountMap.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChartData.add(data);
        }

        UserInMajorPieChart.setData(pieChartData);
        UserInMajorPieChart.setLabelsVisible(false);
        UserInMajorPieChart.setLabelLineLength(0);

        for (PieChart.Data data : UserInMajorPieChart.getData()) {
            data.setName(data.getName() + ": " + (int) data.getPieValue());
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