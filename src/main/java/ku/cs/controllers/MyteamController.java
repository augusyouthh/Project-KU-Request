package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MyteamController {
    private final String thisPage = "my-team";
    @FXML private ImageView backImage;
    @FXML private ImageView groupImage;
    @FXML private ImageView augustyouthImage;
    @FXML private ImageView frameImage;
    @FXML private ImageView peteImage;
    @FXML private ImageView nickImage;
    @FXML private Button teamButton;
    @FXML private Label teamnameLabel;
    @FXML private Button backButton;
    private Scene previousScene;

    @FXML
    public void initialize(){
        Image back = new Image(getClass().getResourceAsStream("/images/Back.png"));
        Image group = new Image(getClass().getResourceAsStream("/images/Group.png"));
        Image augustyouth = new Image(getClass().getResourceAsStream("/images/augustyouth.png"));
        Image frame = new Image(getClass().getResourceAsStream("/images/frame.png"));
        Image pete = new Image(getClass().getResourceAsStream("/images/pete.png"));
        Image nick = new Image(getClass().getResourceAsStream("/images/nick.png"));
        backImage.setImage(back);
        groupImage.setImage(group);
        augustyouthImage.setImage(augustyouth);
        frameImage.setImage(frame);
        peteImage.setImage(pete);
        nickImage.setImage(nick);

    }

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    @FXML
    public void onBackButtonClick() { // Method to go back to previous page
        // ดึง Stage ปัจจุบัน
        Stage stage = (Stage) backButton.getScene().getWindow();
        // ตั้งค่า Scene เดิมกลับไป
        if (previousScene != null) {
            System.out.println("Returning previous scene");
            stage.setScene(previousScene);
        }
        else {
            System.out.println("Previous scene is null");
        }
    }

    public String getThispage() {
        return thisPage;
    }
}
