package ku.cs.controllers.facultyStaff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.models.*;
import ku.cs.services.AppealListDatasource;
import ku.cs.services.AppealSharedData;
import ku.cs.services.ApproveFacultyStaffListDatasource;
import ku.cs.services.FXRouter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class FacultyAppealDetailController {
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
    @FXML private Label approveCheckLabel;

    @FXML
    private TextField declineTextField;
    @FXML private Label FacultyApprovedDate;
    @FXML private Label declineWhen;
    @FXML private Label DeclineDateLabel;
    @FXML private Label declineLabel;
    @FXML private Label FacultyApproveWhen;
    @FXML private Label FacultySignatureLabel;
    @FXML private Label FacultyEndorsers;
    @FXML
    private Label majorApprovedateLabel;
    @FXML
    private Label majorApproveWhen;
    @FXML
    private Label majorSignatureLabel;

    @FXML private Button confirmButton;

    @FXML private Button declineButton;
    @FXML private ChoiceBox<String> approveChoiceBox;
    @FXML private Button onUploadPDFButtonClick;
    @FXML
    private Hyperlink pdfLink;  // Hyperlink ที่จะแสดงไฟล์ PDF
    @FXML private Button downloadPDF;
    private ApproveFacultyStaffListDatasource approveDataSource;

    private AppealList appealList;
    private AppealListDatasource datasource;
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
            declineWhen.setVisible(false);
            DeclineDateLabel.setVisible(false);
            declineLabel.setVisible(false);
            FacultyApproveWhen.setVisible(false);
            FacultySignatureLabel.setVisible(false);
            FacultyApprovedDate.setVisible(false);
            approveCheckLabel.setVisible(false);


            if (appeal.getStatus().contains("ปฏิเสธโดยคณบดี คำร้องถูกปฏิเสธ")) {
                FacultyEndorsers.setVisible(false);
                approveChoiceBox.setVisible(false);
                confirmButton.setVisible(false);
                declineButton.setVisible(false);
                declineWhen.setVisible(true);
                DeclineDateLabel.setVisible(true);
                declineLabel.setText(appeal.getDeclineReason());
                declineLabel.setVisible(true);
                LocalDateTime time = appeal.getDeclineDateTime();
                DeclineDateLabel.setText(time.getDayOfMonth() + "/" + time.getMonth() + "/" + time.getYear() + "  " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
            } else if (appeal.getStatus().equals("อนุมัติโดยคณบดี คำร้องดำเนินการครบถ้วน")) {
                confirmButton.setVisible(false);
                declineButton.setVisible(false);
                FacultyEndorsers.setVisible(false);
                approveChoiceBox.setVisible(false);
                if (appeal.getMajorEndorserDate() != null) {
                    majorApprovedateLabel.setText(appeal.getMajorEndorserDate().toString());
                    majorApprovedateLabel.setVisible(true);
                    majorApproveWhen.setVisible(true);
                    majorSignatureLabel.setText(appeal.getMajorEndorserSignature());
                    majorSignatureLabel.setVisible(true);
                }
                if (appeal.getFacultyEndorserDate() != null) {
                    FacultyApproveWhen.setVisible(true);
                    FacultyApprovedDate.setText(appeal.getFacultyEndorserDate().toString());
                    FacultyApprovedDate.setVisible(true);
                    FacultySignatureLabel.setText(appeal.getFacultyEndorserSignature());
                    FacultySignatureLabel.setVisible(true);
                }
            }
        }

        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;
            loadApproveChoice(user);
        }
        if (user == null){
            System.out.println("user is null");
        }
        // เพิ่มการดักจับการเลือกจาก ChoiceBox
        approveChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && appeal != null) {
                // ดึงชื่อจาก newValue (เช่น "ชื่อ: John Doe, ตำแหน่ง: ...")
                String[] parts = newValue.split(",");
                if (parts.length > 0) {
                    String approverName = parts[0].replace("ชื่อ: ", "").trim();
                    // เซ็ตชื่อคนอนุมัติใน Appeal
                    appeal.setFacultyEndorserSignature(approverName);
                }
            }
        });
    }

    public void loadApproveChoice(User user){
        approveDataSource = new ApproveFacultyStaffListDatasource("data", "approveFacultyStaff.csv");
        approveChoiceBox.getItems().clear();
        ObservableList<String> approveName = FXCollections.observableArrayList();
        for (ApproveFacultyStaff approveFacultyStaff: approveDataSource.readData().getApproveFacultyStaffList()) {
            if (approveFacultyStaff.getFaculty().contains(user.getFaculty())){
                approveName.add("ชื่อ: " + approveFacultyStaff.getName()+ ", ตำแหน่ง: " + approveFacultyStaff.getRole()+ ", คณะ: " + approveFacultyStaff.getFaculty());
            }
        }
        approveChoiceBox.getItems().addAll(approveName);
    }

    @FXML
    public void onApproveAppealClick() {
        downloadPDF.setVisible(true);
        Appeal appeal = AppealSharedData.getSelectedAppeal();

        // ตรวจสอบว่าได้เลือกชื่อคนอนุมัติจาก ChoiceBox แล้วหรือยัง
        if (approveChoiceBox.getValue() == null) {
            approveCheckLabel.setText("กรุณาเลือกผู้อนุมัติก่อน");
            approveCheckLabel.setVisible(true);
            return; // หากยังไม่ได้เลือก ให้หยุดการทำงาน
        }

        // เรียกการอัปโหลดไฟล์ PDF
        FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File("data/appealPDF");  // เปลี่ยนโฟลเดอร์ปลายทางสำหรับไฟล์ PDF
        if (!initialDirectory.exists()) {
            initialDirectory.mkdirs();
        }
        fileChooser.setInitialDirectory(initialDirectory);

        // กำหนด filter ให้เลือกเฉพาะไฟล์ PDF
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        Stage stage = (Stage) confirmButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                File destDir = new File("data/appealPDF");
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                String[] fileSplit = selectedFile.getName().split("\\.");
                String filename = appeal.getAppealID() + "." + fileSplit[fileSplit.length - 1].toLowerCase();
                Path target = FileSystems.getDefault().getPath(destDir.getAbsolutePath() + File.separator + filename);

                Files.copy(selectedFile.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                String pdfFilePath = "data/appealPDF" + File.separator + filename;
                appeal.setPathPDF(pdfFilePath);

                // อัปเดตสถานะคำร้องว่าอนุมัติแล้ว
                appeal.setStatus("อนุมัติโดยคณบดี คำร้องดำเนินการครบถ้วน");
                LocalDate approveDate = LocalDate.now();
                appeal.setFacultyEndorserDate(approveDate);
                long second = new Date().getTime();
                appeal.setSecond(second);

                // บันทึกข้อมูลคำร้องที่เปลี่ยนแปลงลงไฟล์
                AppealListDatasource datasource = new AppealListDatasource("data/appeals.csv");
                AppealList appealList = AppealSharedData.getNormalAppealList();
                datasource.writeData(appealList);

                // เปลี่ยนไปหน้าบุคลากร
                FXRouter.goTo("facultyStaff", user);

            } catch (IOException e) {
                e.printStackTrace();
                approveCheckLabel.setText("เกิดข้อผิดพลาดในการอัปโหลดไฟล์ PDF.");
            }
        } else {
            approveCheckLabel.setText("ไม่มีไฟล์ที่เลือก.");
        }
    }


    @FXML
    public void onDeclineAppealClick(){
        downloadPDF.setVisible(false);
        declineTextField.setVisible(true);
        resetDecline.setVisible(true);
        applyDecline.setVisible(true);
    }

    @FXML
    public void DeclineApplyClick(){

        String DeclineReason = "ถูกปฎิเสธเนื่องจาก" + declineTextField.getText();

        Appeal appeal = AppealSharedData.getSelectedAppeal();
        // ตรวจสอบว่าได้เลือกชื่อคนอนุมัติจาก ChoiceBox แล้วหรือยัง
        if (approveChoiceBox.getValue() == null) {
            approveCheckLabel.setText("กรุณาเลือกผู้อนุมัติก่อน");
            approveCheckLabel.setVisible(true);
            return; // หากยังไม่ได้เลือก ให้หยุดการทำงาน
        }
        if(appeal != null){
            appeal.setDeclineReason(DeclineReason);
            appeal.setStatus("ปฏิเสธโดยคณบดี คำร้องถูกปฏิเสธ");
            LocalDateTime declineDate = LocalDateTime.now();
            appeal.setDeclineDateTime(declineDate);
            long second = new Date().getTime();
            appeal.setSecond(second);
        }
        AppealListDatasource datasource = new AppealListDatasource("data/appeals.csv");
        AppealList appealList = AppealSharedData.getNormalAppealList();

        datasource.writeData(appealList);
        try {
            FXRouter.goTo("facultyStaff", user);
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




    @FXML
    public void onBackButtonClick() {
        try {
            FXRouter.goTo("facultyStaff", user);
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
            approveCheckLabel.setText("ไม่พบไฟล์ PDF.");
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
                approveCheckLabel.setText("ไม่พบไฟล์ PDF.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            approveCheckLabel.setText("ไม่สามารถเปิดไฟล์ PDF ได้.");
        }
    }
}