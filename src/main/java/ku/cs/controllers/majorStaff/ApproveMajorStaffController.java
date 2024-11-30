package ku.cs.controllers.majorStaff;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.MajorEndorserListFileDatasource;

import java.io.File;
import java.io.IOException;

public class ApproveMajorStaffController {
    @FXML
    private TableView<MajorEndorser> approveMajorStaffTableView;

    private MajorEndorserList majorEndorserList;
    private Datasource<MajorEndorserList> datasource;

    private User user;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Button toggleSidebarButton; // ปุ่มสำหรับแสดง/ซ่อน Sidebar
    @FXML private Circle imagecircle;

    @FXML
    public void initialize() {
        datasource = new MajorEndorserListFileDatasource("data", "major-endorser.csv");
        majorEndorserList = datasource.readData();
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;
            showTable(majorEndorserList);
        }
        String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
        String url = new File(imagePath).toURI().toString();
        imagecircle.setFill(new ImagePattern(new Image(url)));
    }

    private void showTable(MajorEndorserList majorEndorserList) {

        TableColumn<MajorEndorser, String> nameColumn = new TableColumn<>("ชื่อ");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MajorEndorser, String> roleColumn = new TableColumn<>("ตำแหน่ง");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("position"));


        approveMajorStaffTableView.getColumns().clear();
        approveMajorStaffTableView.getColumns().add(nameColumn);
        approveMajorStaffTableView.getColumns().add(roleColumn);

        approveMajorStaffTableView.getItems().clear();

        for (MajorEndorser majorEndorser : majorEndorserList.getMajorEndorsers()) {
            if (majorEndorser.getPosition().contains(user.getMajor())) {
                approveMajorStaffTableView.getItems().add(majorEndorser); // เพิ่มรายการใน TableView
            }
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
