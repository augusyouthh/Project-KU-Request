package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import ku.cs.controllers.components.AppealItemController;
import ku.cs.models.Appeal;
import ku.cs.models.AppealList;
import ku.cs.models.User;
import ku.cs.services.AppealSharedData;
import ku.cs.services.AppealListDatasource;
import ku.cs.services.AppealSortComparator;
import ku.cs.services.FXRouter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;


public class AppealListController {


    @FXML
    private VBox appealVBox;

    @FXML
    private Label noAppealsLabel;

    @FXML
    private TextField searchTextField;

    private AppealListDatasource datasource;
    private AppealList appealList;
    private User user;

    @FXML
    public void initialize() {
        datasource = new AppealListDatasource("data/appeals.csv");
        appealList = datasource.readData();
        AppealSharedData.setNormalAppealList(appealList);

        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;
        }
        loadAppeals(null, null);
    }
    @FXML
    public void onBackButtonClick() {
        try {
            FXRouter.goTo("student", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void loadAppeals(String filterType, String searchQuery) {

        appealList = AppealSharedData.getNormalAppealList();
        List<Appeal> appeals = appealList.getsAppeals();

        if (user != null) {
            appeals = appeals.stream()
                    .filter(appeal -> appeal.getStudentID() != null && appeal.getStudentID().equals(user.getId()))
                    .collect(Collectors.toList());
        }

        if (filterType != null) {
            appeals = appeals.stream()
                    .filter(appeal -> appeal.getType().equals(filterType))
                    .collect(Collectors.toList());
        }

        if (searchQuery != null && !searchQuery.isEmpty()) {
            String lowerCaseQuery = searchQuery.toLowerCase();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            appeals = appeals.stream()
                    .filter(appeal ->
                            appeal.getSubject().toLowerCase().contains(lowerCaseQuery) || //เสิชหัวข้อคำร้องได้
                                    appeal.getRequest().toLowerCase().contains(lowerCaseQuery) || //เนื้อหาคำร้อง
                                    appeal.getDate().format(dateFormatter).contains(lowerCaseQuery) || //เสิชจากวันที่
                                    appeal.getStudentSignature().toLowerCase().contains(lowerCaseQuery) //เสิชจากผู้ลงนาม
                    ).collect(Collectors.toList());
        }

        appeals.sort(new AppealSortComparator(filterType));

        if(appeals.isEmpty()){
            noAppealsLabel.setVisible(true);
        }else{
            noAppealsLabel.setVisible(false);
            appealVBox.getChildren().clear();
            for (Appeal appeal : appeals) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/student/appeal-item.fxml"));
                    Pane pane = loader.load();

                    AppealItemController controller = loader.getController();
                    controller.setAppealData(appeal);

                    appealVBox.getChildren().add(pane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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




}
