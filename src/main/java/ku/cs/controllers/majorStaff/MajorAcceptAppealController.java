package ku.cs.controllers.majorStaff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import ku.cs.models.Appeal;
import ku.cs.models.AppealList;
import ku.cs.models.MajorEndorser;
import ku.cs.models.User;
import ku.cs.services.AppealListDatasource;
import ku.cs.services.AppealSharedData;
import ku.cs.services.FXRouter;
import ku.cs.services.MajorEndorserListFileDatasource;


import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class MajorAcceptAppealController {
    private User user;

    @FXML
    private Label typeLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private Label requestLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label errorLabel;

    @FXML
    private ChoiceBox<String> endorserBox;
    @FXML private TextField declineTextField;
    @FXML private CheckBox sendingToDean;
    @FXML private Label majorSignatureLabel;
    @FXML private Label majorApprovedateLabel;
    @FXML private Button applyDeclineButton;
    @FXML private Button approveAppealButton;
    @FXML private Button declineButton;
    @FXML private Label signatureLabel;
    @FXML private Label declineLabel;
    @FXML private Label majorApproveWhen;
    @FXML private Label declineWhen;
    @FXML private Label DeclineDateLabel;
    @FXML private Label MajorEndorsers;
    @FXML
    private Label facultySignatureLabel;
    @FXML
    private Label facultyApproveWhen;
    @FXML
    private Label facultyApprovedateLabel;
    @FXML
    private Hyperlink pdfLink;  // Hyperlink ที่จะแสดงไฟล์ PDF
    @FXML private Button downloadPDF;

    private MajorEndorserListFileDatasource approveDataSource;

    private AppealList appealList;
    private AppealListDatasource datasource;
    private String studentID;
    private Appeal appeal;
    public void initialize() {

        appeal = AppealSharedData.getSelectedAppeal();
        if (appeal != null) {
            typeLabel.setText(appeal.getType());
            subjectLabel.setText(appeal.getSubject());
            requestLabel.setText(appeal.getRequest());
            dateLabel.setText(appeal.getDate().toString());
            signatureLabel.setText(appeal.getStudentSignature());


            if(appeal.getStatus().contains("ปฏิเสธ")){

                MajorEndorsers.setVisible(false);
                declineWhen.setVisible(true);
                LocalDateTime time = appeal.getDeclineDateTime();
                DeclineDateLabel.setText(time.getDayOfMonth() + "/" + time.getMonth() + "/" + time.getYear() + "  " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
                DeclineDateLabel.setVisible(true);
                declineLabel.setText(appeal.getDeclineReason());
                declineLabel.setVisible(true);
            }
            if (appeal.getStatus().contains("อนุมัติ")){
                if (appeal.getMajorEndorserDate() != null){

                    majorApprovedateLabel.setText(appeal.getMajorEndorserDate().toString());
                    majorApprovedateLabel.setVisible(true);
                    majorApproveWhen.setVisible(true);
                    majorSignatureLabel.setText(appeal.getMajorEndorserSignature());
                    majorSignatureLabel.setVisible(true);
                }
                if (appeal.getFacultyEndorserDate() != null){
                    facultyApprovedateLabel.setText(appeal.getFacultyEndorserDate().toString());
                    facultyApprovedateLabel.setVisible(true);
                    facultyApproveWhen.setVisible(true);
                    facultySignatureLabel.setText(appeal.getFacultyEndorserSignature());
                    facultySignatureLabel.setVisible(true);
                }
            }

        }

        // โหลดรายชื่อจากไฟล์ CSV ลงใน ChoiceBox
        loadEndorsersFromCSV("data/major-endorser.csv");

        if(appeal.getStatus().contains("โดยหัวหน้าภาควิชา")){
            applyDeclineButton.setVisible(false);
            approveAppealButton.setVisible(false);
            endorserBox.setVisible(false);
            MajorEndorsers.setVisible(false);
            sendingToDean.setVisible(false);
            declineButton.setVisible(false);
        }

        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;
            loadApproveChoice(user);
        }

    }

    public void loadApproveChoice(User user){
        approveDataSource = new MajorEndorserListFileDatasource("data", "major-endorser.csv");
        endorserBox.getItems().clear();
        ObservableList<String> approveName = FXCollections.observableArrayList();
        for (MajorEndorser majorEndorser: approveDataSource.readData().getMajorEndorsers()) {
            if (majorEndorser.getPosition().contains(user.getMajor())) {
                approveName.add(majorEndorser.getName() + " " + majorEndorser.getPosition());
            }
        }

        endorserBox.getItems().addAll(approveName);
    }


    @FXML
    public void onApplyAppealClick() {
        if (appeal != null) {
            String endorserValue = endorserBox.getValue();
            LocalDate today = LocalDate.now();
            String majorName = user.getMajor();
            long second = new Date().getTime();
            appeal.setSecond(second);
            declineTextField.setVisible(false);
            applyDeclineButton.setVisible(false);

            // ตรวจสอบการเลือกผู้รับรอง
            if (endorserValue == null || endorserValue.isEmpty()) {
                errorLabel.setVisible(true);
                errorLabel.setText("กรุณาเลือกผู้รับรองก่อนดำเนินการอนุมัติ.");
                return;
            }

            // ตรวจสอบว่า endorserValue เป็นผู้รับรองที่ถูกต้อง
            if (!endorserValue.contains(majorName)) {
                errorLabel.setVisible(true);
                errorLabel.setText("ผู้รับรองไม่ตรงกับสาขาของคุณ.");
                return;
            }

            // ตั้งค่าใน appeal
            appeal.setMajorEndorserSignature(endorserValue);
            appeal.setMajorEndorserDate(today);
            appeal.setDeclineReason("");

            // กำหนดสถานะ
            if (sendingToDean.isSelected()) {
                appeal.setStatus("อนุมัติโดยหัวหน้าภาควิชา คำร้องส่งต่อให้คณบดี");
            } else {
                appeal.setStatus("อนุมัติโดยหัวหน้าภาควิชา คำร้องดำเนินการครบถ้วน");
            }

            // อัปโหลด PDF
            FileChooser fileChooser = new FileChooser();
            File initialDirectory = new File("data/appealPDF");
            if (!initialDirectory.exists()) {
                initialDirectory.mkdirs();
            }
            fileChooser.setInitialDirectory(initialDirectory);
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            Stage stage = (Stage) approveAppealButton.getScene().getWindow();
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

                    // แสดงข้อความสำเร็จ
                    showAlert("อัปโหลดไฟล์ PDF สำเร็จ", "ไฟล์ถูกอัปโหลดเรียบร้อยแล้ว!");

                    // บันทึกข้อมูล appeal ลง CSV
                    AppealListDatasource datasource = new AppealListDatasource("data/appeals.csv");
                    AppealList appealList = AppealSharedData.getNormalAppealList();
                    datasource.writeData(appealList);

                    // เปลี่ยนไปยังมุมมอง staff
                    try {
                        FXRouter.goTo("departmentStaff", user);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    errorLabel.setText("เกิดข้อผิดพลาดในการอัพโหลดไฟล์ PDF.");
                    errorLabel.setVisible(true);
                }
            } else {
                errorLabel.setText("ไม่มีไฟล์ที่เลือก.");
                errorLabel.setVisible(true);
            }
        }
    }


    @FXML
    public void DeclineApplyClick(){

        declineTextField.setVisible(true);
        applyDeclineButton.setVisible(true);
    }

    @FXML
    public void onApplyDeclineButton(){
        String DeclineReason = "ถูกปฎิเสธเนื่องจาก: " + declineTextField.getText();

        Appeal appeal = AppealSharedData.getSelectedAppeal();
        if(appeal != null){
            appeal.setDeclineReason(DeclineReason);
            appeal.setMajorEndorserSignature(endorserBox.getValue());
            appeal.setStatus("ปฏิเสธโดยหัวหน้าภาควิชา คำร้องถูกปฏิเสธ");
            appeal.setMajorEndorserDate(LocalDate.now());
            appeal.setDeclineDateTime(LocalDateTime.now());
            long second = new Date().getTime();
            appeal.setSecond(second);
        }
        AppealListDatasource datasource = new AppealListDatasource("data/appeals.csv");
        AppealList appealList = AppealSharedData.getNormalAppealList();
        datasource.writeData(appealList);

        try {
            FXRouter.goTo("departmentStaff", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    // ฟังก์ชันสำหรับโหลดรายชื่อจาก CSV และเพิ่มเข้า ChoiceBox
    private void loadEndorsersFromCSV(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // แทนที่เครื่องหมาย , ด้วยช่องว่าง
                String replacedLine = line.replace(",", " ");
                // เพิ่มข้อมูลลงใน ChoiceBox
                endorserBox.getItems().add(replacedLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void onBackButton() {
        try {
            if (user.getRole().equals("student")) {
                FXRouter.goTo("appeal-tracking", user);
            } else if (user.getRole().equals("advisor")) {
                FXRouter.goTo("advisor-appeal-page", user);
            } else if (user.getRole().equals("majorStaff")) {
                FXRouter.goTo("departmentStaff", user);
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
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}