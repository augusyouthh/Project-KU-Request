package ku.cs.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import ku.cs.models.Appeal;
import ku.cs.models.User;
import ku.cs.services.AppealSharedData;
import ku.cs.services.FXRouter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;


public class AppealDetailController {
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
    private Label declineLabel;
    @FXML
    private Label DeclineDateLabel;
    @FXML
    private Label majorApprovedateLabel;
    @FXML
    private Label facultyApprovedateLabel;
    @FXML
    private Label declineWhen;
    @FXML
    private Label majorApproveWhen;
    @FXML
    private Label facultyApproveWhen;
    @FXML
    private Label majorSignatureLabel;
    @FXML
    private Label facultySignatureLabel;
    @FXML
    private Hyperlink pdfLink;  // Hyperlink ที่จะแสดงไฟล์ PDF
    @FXML private Button downloadPDF;
    @FXML private Label errorLabel;
    private User user;


    @FXML
    public void initialize() {
        Appeal appeal = AppealSharedData.getSelectedAppeal();
        if (appeal != null) {
            typeLabel.setText(appeal.getType());
            subjectLabel.setText(appeal.getSubject());
            requestLabel.setText(appeal.getRequest());
            dateLabel.setText(appeal.getDate().toString());
            signatureLabel.setText(appeal.getStudentSignature());

            if(appeal.getDeclineDateTime() != null && appeal.getStatus().contains("ปฏิเสธ")){
                declineWhen.setVisible(true);
                LocalDateTime time = appeal.getDeclineDateTime();
                DeclineDateLabel.setText(time.getDayOfMonth() + "/" + time.getMonth() + "/" + time.getYear() + "  " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
                DeclineDateLabel.setVisible(true);
                declineLabel.setText(appeal.getDeclineReason());
                declineLabel.setVisible(true);
            }
            if (appeal.getMajorEndorserDate() != null && appeal.getStatus().contains("อนุมัติ")){
                majorApprovedateLabel.setText(appeal.getMajorEndorserDate().toString());
                majorApprovedateLabel.setVisible(true);
                majorApproveWhen.setVisible(true);
                majorSignatureLabel.setText(appeal.getMajorEndorserSignature());
                majorSignatureLabel.setVisible(true);
            }
            if (appeal.getFacultyEndorserDate() != null && appeal.getStatus().contains("อนุมัติ")){
                facultyApprovedateLabel.setText(appeal.getFacultyEndorserDate().toString());
                facultyApprovedateLabel.setVisible(true);
                facultyApproveWhen.setVisible(true);
                facultySignatureLabel.setText(appeal.getFacultyEndorserSignature());
                facultySignatureLabel.setVisible(true);
            }



        }
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;
        }
    }

    @FXML
    public void onBackButtonClick() {
        try {
            if(user.getRole().equals("student")){
                FXRouter.goTo("appeal-tracking", user);
            }

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