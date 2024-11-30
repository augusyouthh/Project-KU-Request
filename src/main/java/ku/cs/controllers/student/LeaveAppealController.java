package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.scene.control.SpinnerValueFactory;
import ku.cs.models.AppealList;
import ku.cs.services.FXRouter;
import java.io.IOException;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import ku.cs.models.Appeal;
import ku.cs.models.User;
import java.time.LocalDate;
import javafx.scene.control.Label;
import ku.cs.services.AppealSharedData;

import java.time.LocalDateTime;

import ku.cs.services.AppealListDatasource;

import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

public class LeaveAppealController {
    @FXML
    private Label ErrorLabel;

    @FXML
    private TextField courseTextfield;

    @FXML
    private Spinner<Integer> daySpinner;

    @FXML
    private TextField endSemesterfield;

    @FXML
    private Spinner<Integer> endyearSpinner;

    @FXML
    private Spinner<Integer> monthSpinner;

    @FXML
    private TextField nyearsTextField;

    @FXML
    private TextField signatureTextField;

    @FXML
    private TextField startSemesterfield;

    @FXML
    private Spinner<Integer> startyearSpinner;

    @FXML
    private TextField subjectTextField;

    @FXML
    private Spinner<Integer> yearSpinner;

    private AppealListDatasource datasource;

    private User user;

    @FXML
    public void initialize() {
        daySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, LocalDate.now().getDayOfMonth()));
        monthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, LocalDate.now().getMonthValue()));
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, LocalDate.now().getYear(), LocalDate.now().getYear()));
        endyearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2567,3000, LocalDate.now().getYear()));
        startyearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2567, 3000, LocalDate.now().getYear()));

        datasource = new AppealListDatasource("data/appeals.csv");
        AppealList loadedAppeals = datasource.readData();
        AppealSharedData.setNormalAppealList(loadedAppeals);

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

        ErrorLabel.setVisible(false);

        String studentID = user.getId();
        String type = "ใบลาพักการศึกษา:";
        String subject = subjectTextField.getText();
        String nYears = nyearsTextField.getText();
        String startSemester = startSemesterfield.getText();
        String startYear = String.valueOf(startyearSpinner.getValue());
        String endSemester = endSemesterfield.getText();
        String endYear = String.valueOf(endyearSpinner.getValue());
        String courseDetails = courseTextfield.getText();
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

        int day = daySpinner.getValue();
        int month = monthSpinner.getValue();
        int year = yearSpinner.getValue();
        long second = new Date().getTime();
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.of(year, month, day);

        String request = "มีความประสงค์ขอลาพักการศึกษาเป็นจำนวน " + nYears + " ปีการศึกษา ตั้งแต่ภาค " +
                startSemester + " ปีการศึกษา " + startYear + " ถึงภาค " + endSemester +
                " ปีการศึกษา " + endYear + " วิชาที่ลงทะเบียน: " + courseDetails;

        if (subject.isEmpty() || nYears.isEmpty() || startSemester.isEmpty() || endSemester.isEmpty() ||
                courseDetails.isEmpty() ||studentSignature.isEmpty()) {
            ErrorLabel.setText("Please fill out all fields.");
            ErrorLabel.setVisible(true);
            return;
        }
        try {
            Appeal appeal = new Appeal(studentID ,type , subject, request, date, studentSignature, second, status, time, declineReason, majorEndorserSignature, majorDate, FacultyDate, DeclineDatetime, FacultyEndorserSignature, appealID, pathPDF);
            AppealSharedData.getNormalAppealList().addAppeal(appeal);
            datasource.writeData(AppealSharedData.getNormalAppealList());
            clearFields();

        } catch (NumberFormatException e) {
            ErrorLabel.setVisible(true);
        }

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
        nyearsTextField.clear();
        startSemesterfield.clear();
        endSemesterfield.clear();
        courseTextfield.clear();
        signatureTextField.clear();
        daySpinner.getValueFactory().setValue(LocalDate.now().getDayOfMonth());
        monthSpinner.getValueFactory().setValue(LocalDate.now().getMonthValue());
        yearSpinner.getValueFactory().setValue(LocalDate.now().getYear());
    }
    private String generateRandomAppealID(int length) {
        String uuid = UUID.randomUUID().toString().replace("-", ""); // สร้าง UUID และลบเครื่องหมาย "-"
        return uuid.substring(0, Math.min(length, uuid.length())) ; // ตัดความยาวและเพิ่มนามสกุล
    }
}