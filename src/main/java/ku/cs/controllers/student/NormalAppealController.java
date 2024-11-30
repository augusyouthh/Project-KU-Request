package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.scene.control.SpinnerValueFactory;
import ku.cs.models.User;
import ku.cs.services.FXRouter;
import java.io.IOException;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import ku.cs.models.Appeal;
import ku.cs.models.AppealList;
import java.time.LocalDate;
import javafx.scene.control.Label;
import ku.cs.services.AppealSharedData;
import javafx.scene.control.CheckBox;

import ku.cs.services.AppealListDatasource;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

public class NormalAppealController {
    @FXML
    private TextField subjectTextField;
    @FXML
    private TextField requestTextField;
    @FXML
    private Spinner<Integer> daySpinner;
    @FXML
    private Spinner<Integer> monthSpinner;
    @FXML
    private Spinner<Integer> yearSpinner;
    @FXML
    private TextField signatureTextField;
    @FXML
    private Label ErrorLabel;
    @FXML
    private CheckBox lostCheckBox; // สูญหาย
    @FXML
    private CheckBox damagedCheckBox; // ชำรุด

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

        lostCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                damagedCheckBox.setSelected(false);
            }
        });

        damagedCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lostCheckBox.setSelected(false);
            }
        });

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
    public void onApplyAppealClick() {
        String studentID = user.getId();
        String type = "คำร้องทั่วไป:";
        String subject = subjectTextField.getText();
        String request = requestTextField.getText();
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
        String appealID = generateRandomAppealID(6);

        if (subject.isEmpty() || request.isEmpty() || studentSignature.isEmpty()) {
            ErrorLabel.setVisible(true);
            return;
        }
        if (lostCheckBox.isSelected()) {
            request += " เอกสารสูญหาย";
        }
        if (damagedCheckBox.isSelected()) {
            request += " เอกสารชำรุด";
        }
        ErrorLabel.setVisible(false);
        subject = subject.replace(",", " ");
        Appeal appeal = new Appeal(studentID ,type , subject, request, date, studentSignature, second, status, time, declineReason, majorEndorserSignature, majorDate, FacultyDate, DeclineDatetime, FacultyEndorserSignature, appealID,pathPDF);
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
        subjectTextField.clear();
        requestTextField.clear();
        daySpinner.getValueFactory().setValue(LocalDate.now().getDayOfMonth());
        monthSpinner.getValueFactory().setValue(LocalDate.now().getMonthValue());
        yearSpinner.getValueFactory().setValue(LocalDate.now().getYear());
        signatureTextField.clear();
        lostCheckBox.setSelected(false);
        damagedCheckBox.setSelected(false);
    }
    private String generateRandomAppealID(int length) {
        String uuid = UUID.randomUUID().toString().replace("-", ""); // สร้าง UUID และลบเครื่องหมาย "-"
        return uuid.substring(0, Math.min(length, uuid.length())) ; // ตัดความยาวและเพิ่มนามสกุล
    }

}