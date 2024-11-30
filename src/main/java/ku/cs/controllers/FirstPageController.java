package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.services.FXRouter;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FirstPageController {
    @FXML
    ImageView logoImage;
    @FXML
    ImageView kulogo;
    @FXML
    ImageView rightImage;
    @FXML
    ImageView leftImage;

    @FXML
    public void initialize(){
        Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
        Image ku_logo = new Image(getClass().getResourceAsStream("/images/ku-logo.png"));
        Image right_image = new Image(getClass().getResourceAsStream("/images/Group-33624.png"));
        Image left_image = new Image(getClass().getResourceAsStream("/images/Group-33625.png"));
        logoImage.setImage(logo);
        kulogo.setImage(ku_logo);
        rightImage.setImage(right_image);
        leftImage.setImage(left_image);
    }
    @FXML
    public void onStartButtonClick() {
        try {
            FXRouter.goTo("login-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void IntroductionToProgramUsage() {
        try {
            File pdfFile = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator +"InstructionMYMEE.pdf");

            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                System.out.println("ไฟล์ PDF ไม่พบ: " + pdfFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
