package ku.cs.controllers.student;

import javafx.fxml.FXML;
import ku.cs.models.AppealList;
import ku.cs.services.FXRouter;
import java.io.IOException;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Label;
import ku.cs.models.Appeal;
import ku.cs.models.User;
import java.time.LocalDate;
import ku.cs.services.AppealSharedData;

import java.time.LocalDateTime;
import java.util.Date;
import java.time.LocalTime;
import java.util.UUID;

import ku.cs.services.AppealListDatasource;

public class EnrollAppealController {
    @FXML
    private Label ErrorLabel;

    @FXML
    private Spinner<Integer> daySpinner;

    @FXML
    private Spinner<Integer> monthSpinner;

    @FXML
    private Spinner<Integer> yearSpinner;

    @FXML
    private TextField signatureTextField;

    @FXML
    private TextField requestTextField;

    @FXML
    private CheckBox lateRegisCheck; //ลงทะเบียนล่าช้า
    @FXML
    private CheckBox lateAddDropCheck; //เพิ่มถอนวิชาล่าช่้า
    @FXML
    private CheckBox OverRegisCheck; //ลงทะเบียนเกิน
    @FXML
    private CheckBox LessRegisCheck; //ลงทะเบียนตํ่ากว่า
    @FXML
    private CheckBox PostPayCheck; //ผ่อนผันค่าธรรมเนียม
    @FXML
    private CheckBox TransferMajorCheck; //ย้ายคณะหรือสาขา

    private AppealListDatasource datasource;

    private User user;

    @FXML
    public void initialize() {
        daySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, LocalDate.now().getDayOfMonth()));
        monthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, LocalDate.now().getMonthValue()));
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, LocalDate.now().getYear(), LocalDate.now().getYear()));

        datasource = new AppealListDatasource("data/appeals.csv");
        AppealList loadedAppeals = datasource.readData();
        AppealSharedData.setNormalAppealList(loadedAppeals);

        lateRegisCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                deselectOthers(lateRegisCheck);
            }
        });
        lateAddDropCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                deselectOthers(lateAddDropCheck);
            }
        });
        OverRegisCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                deselectOthers(OverRegisCheck);
            }
        });
        LessRegisCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                deselectOthers(LessRegisCheck);
            }
        });
        PostPayCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                deselectOthers(PostPayCheck);
            }
        });
        TransferMajorCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                deselectOthers(TransferMajorCheck);
            }
        });

        ErrorLabel.setVisible(false);
    }
    private void deselectOthers(CheckBox selectedCheckBox) {
        if (selectedCheckBox != lateRegisCheck) lateRegisCheck.setSelected(false);
        if (selectedCheckBox != lateAddDropCheck) lateAddDropCheck.setSelected(false);
        if (selectedCheckBox != OverRegisCheck) OverRegisCheck.setSelected(false);
        if (selectedCheckBox != LessRegisCheck) LessRegisCheck.setSelected(false);
        if (selectedCheckBox != PostPayCheck) PostPayCheck.setSelected(false);
        if (selectedCheckBox != TransferMajorCheck) TransferMajorCheck.setSelected(false);


        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;
        }
    }

    @FXML
    public void onBackButtonClick() {
        try {
            FXRouter.goTo("student");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onApplyAppealClick(){

        String studentID = user.getId();
        String type = "ขอลงทะเบียนเรียน:";
        String subject = "มีความประสงค์: ";
        String request =  requestTextField.getText();
        int day = daySpinner.getValue();
        int month = monthSpinner.getValue();
        int year = yearSpinner.getValue();
        long second = new Date().getTime();
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.of(year, month, day);
        String studentSignature = signatureTextField.getText();
        String status = "ใบคำร้องใหม่ คำร้องส่งต่อให้อาจารย์ที่ปรึกษา";
        String declineReason = "";
        String majorEndorserSignature = "";
        LocalDate majorDate = null;
        LocalDate FacultyDate = null;
        String FacultyEndorserSignature = "";
        LocalDateTime DeclineDatetime = null;
        String pathPDF = null;
        String appealID = generateRandomAppealId(6);

        if (!lateRegisCheck.isSelected() && !lateAddDropCheck.isSelected() && !OverRegisCheck.isSelected() &&
                !LessRegisCheck.isSelected() && !PostPayCheck.isSelected() && !TransferMajorCheck.isSelected()) {
            ErrorLabel.setVisible(true);
            return;
        }

        if(lateRegisCheck.isSelected()){
            subject += "ลงทะเบียนเรียนล่าช้าหรือรักษาสถานภาพนิสิตล่าช้า";
        }
        if(lateAddDropCheck.isSelected()){
            subject += "เพิ่มหรือถอนรายวิชาล่าช้า";
        }
        if(OverRegisCheck.isSelected()){
            subject += "ลงทะเบียนเกิน 22 หริอ 7 หน่วยกิต";
        }
        if(LessRegisCheck.isSelected()){
            subject += "ลงทะเบียนต่ากว่า 9 หน่วยกิต";
        }
        if(PostPayCheck.isSelected()){
            subject += "ผ่อนผันค่าธรรมเนียมการศึกษา";
        }
        if(TransferMajorCheck.isSelected()){
            subject += "ย้ายคณะ หรือเปลี่ยนสาขาวิชาเอก";
        }
        ErrorLabel.setVisible(false);

        Appeal appeal = new Appeal(studentID ,type , subject, request, date, studentSignature, second, status, time, declineReason, majorEndorserSignature, majorDate, FacultyDate, DeclineDatetime, FacultyEndorserSignature, appealID, pathPDF);
        AppealSharedData.getNormalAppealList().addAppeal(appeal);
        datasource.writeData(AppealSharedData.getNormalAppealList());
        clearFields();

        try {
            FXRouter.goTo("student");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onResetAppealClick() {
        clearFields();
    }

    private void clearFields() {
        requestTextField.clear();
        daySpinner.getValueFactory().setValue(LocalDate.now().getDayOfMonth());
        monthSpinner.getValueFactory().setValue(LocalDate.now().getMonthValue());
        yearSpinner.getValueFactory().setValue(LocalDate.now().getYear());
        signatureTextField.clear();
        lateRegisCheck.setSelected(false);
        lateAddDropCheck.setSelected(false);
        OverRegisCheck.setSelected(false);
        LessRegisCheck.setSelected(false);
        PostPayCheck.setSelected(false);
        TransferMajorCheck.setSelected(false);
    }
    private String generateRandomAppealId(int length) {
        String uuid = UUID.randomUUID().toString().replace("-", ""); // สร้าง UUID และลบเครื่องหมาย "-"
        return uuid.substring(0, Math.min(length, uuid.length())) ; // ตัดความยาวและเพิ่มนามสกุล
    }


}