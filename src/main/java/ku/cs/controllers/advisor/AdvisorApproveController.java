package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import ku.cs.models.Appeal;
import ku.cs.models.AppealList;
import ku.cs.models.User;
import ku.cs.services.AppealListDatasource;
import ku.cs.services.AppealSharedData;
import ku.cs.services.FXRouter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class AdvisorApproveController {
    @FXML
    private Label typeLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private Label requestLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label signatureLabel;
    @FXML
    private Button resetDecline;
    @FXML
    private Button applyDecline;

    @FXML Label declineWhen;

    @FXML Label DeclineDateLabel;
    @FXML Label DeclineReasonLabel;

    @FXML
    private Label majorApprovedateLabel;
    @FXML
    private Label facultyApprovedateLabel;
    @FXML
    private Label majorApproveWhen;
    @FXML
    private Label facultyApproveWhen;
    @FXML
    private Label majorSignatureLabel;
    @FXML
    private Label facultySignatureLabel;


    @FXML private TextField declineTextField;

    @FXML private Button confirmButton;

    @FXML private Button declineButton;
    @FXML
    private Hyperlink pdfLink;  // Hyperlink ที่จะแสดงไฟล์ PDF
    @FXML private Button downloadPDF;
    @FXML private Label errorLabel;

    private User user;

    private String studentID;

    @FXML
    public void initialize() {
        setDefault();
        Appeal appeal = AppealSharedData.getSelectedAppeal();
        if (appeal != null) {
            typeLabel.setText(appeal.getType());
            subjectLabel.setText(appeal.getSubject());
            requestLabel.setText(appeal.getRequest());
            dateLabel.setText(appeal.getDate().toString());
            signatureLabel.setText(appeal.getStudentSignature());

            if(appeal.getStatus().contains("ปฏิเสธ")){
                confirmButton.setVisible(false);
                declineButton.setVisible(false);
                declineWhen.setVisible(true);
                DeclineDateLabel.setVisible(true);
                DeclineReasonLabel.setText(appeal.getDeclineReason());
                DeclineReasonLabel.setVisible(true);
                LocalDateTime time = appeal.getDeclineDateTime();
                DeclineDateLabel.setText(time.getDayOfMonth() + "/" + time.getMonth() + "/" + time.getYear() + "  " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
            } else if(appeal.getStatus().contains("อนุมัติ")) {
                confirmButton.setVisible(false);
                declineButton.setVisible(false);
                if (appeal.getMajorEndorserDate() != null ){
                    majorApprovedateLabel.setText(appeal.getMajorEndorserDate().toString());
                    majorApprovedateLabel.setVisible(true);
                    majorApproveWhen.setVisible(true);
                    majorSignatureLabel.setText(appeal.getMajorEndorserSignature());
                    majorSignatureLabel.setVisible(true);
                }
                if (appeal.getFacultyEndorserDate() != null ){
                    facultyApprovedateLabel.setText(appeal.getFacultyEndorserDate().toString());
                    facultyApprovedateLabel.setVisible(true);
                    facultyApproveWhen.setVisible(true);
                    facultySignatureLabel.setText(appeal.getFacultyEndorserSignature());
                    facultySignatureLabel.setVisible(true);
                }
            }
        }

        Object data = FXRouter.getData();
        if (data instanceof Pair) {
            Pair<User, String> pair = (Pair<User, String>) data;
            user = pair.getKey();
            studentID = pair.getValue();
        }
    }

    @FXML
    public void onApproveAppealClick(){
        Appeal appeal = AppealSharedData.getSelectedAppeal();
        if(appeal != null){
            appeal.setStatus("อนุมัติโดยอาจารย์ที่ปรึกษา คำร้องส่งต่อให้หัวหน้าภาควิชา");
            long second = new Date().getTime();
            appeal.setSecond(second);
        }
        AppealListDatasource datasource = new AppealListDatasource("data/appeals.csv");
        AppealList appealList = AppealSharedData.getNormalAppealList();

        datasource.writeData(appealList);
        try {
            FXRouter.goTo("advisor-appeal-page", new Pair<>(user, appeal.getStudentID()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onDeclineAppealClick(){
        declineTextField.setVisible(true);
        resetDecline.setVisible(true);
        applyDecline.setVisible(true);
    }

    @FXML
    public void DeclineApplyClick(){
        String DeclineReason = "ถูกปฎิเสธเนื่องจาก" + declineTextField.getText();

        Appeal appeal = AppealSharedData.getSelectedAppeal();
        if(appeal != null){
            appeal.setDeclineReason(DeclineReason);
            appeal.setStatus("ปฏิเสธโดยอาจารย์ที่ปรึกษา คำร้องถูกปฏิเสธ");
            long second = new Date().getTime();
            appeal.setSecond(second);
            LocalDateTime declineDate = LocalDateTime.now();
            appeal.setDeclineDateTime(declineDate);
        }
        AppealListDatasource datasource = new AppealListDatasource("data/appeals.csv");
        AppealList appealList = AppealSharedData.getNormalAppealList();

        datasource.writeData(appealList);
        try {
            FXRouter.goTo("advisor-appeal-page", new Pair<>(user, studentID));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onResetDeclineClick(){
        clearFields();
    }

    private void clearFields() {
       declineTextField.clear();
    }

    private void setDefault(){
        confirmButton.setVisible(true);
        declineButton.setVisible(true);
    }


    @FXML
    public void onBackButtonClick() {
        try {
            FXRouter.goTo("advisor-appeal-page", new Pair<>(user, studentID));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void showPDFInProject() {
        Appeal appeal = AppealSharedData.getSelectedAppeal();
        // กำหนด path ของไฟล์ PDF ที่อยู่ในโฟลเดอร์ของโปรเจค
        String pdfFilePath = appeal.getPathPDF();

        File pdfFile = new File(pdfFilePath);

        if (pdfFile.exists()) {
            // แสดงลิงก์ไฟล์ PDF ใน UI
            pdfLink.setText("เปิดไฟล์ PDF");
            pdfLink.setVisible(true);

            // ตั้งค่าให้เมื่อคลิกลิงก์จะเปิดไฟล์ PDF
            pdfLink.setOnAction(event -> openPDF(pdfFilePath));
        } else {
            errorLabel.setText("ไม่พบไฟล์ PDF.");
        }
        downloadPDF.setVisible(false);
    }

    // Method สำหรับเปิดไฟล์ PDF ในโปรแกรมดู PDF ภายนอก
    private void openPDF(String filePath) {
        try {
            File pdfFile = new File(filePath);
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);  // เปิดไฟล์ PDF ด้วยโปรแกรมที่ติดตั้งในเครื่อง
            } else {
                errorLabel.setText("ไม่พบไฟล์ PDF.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("ไม่สามารถเปิดไฟล์ PDF ได้.");
        }
    }
}
