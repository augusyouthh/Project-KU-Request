package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import ku.cs.services.FXRouter;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class HelloController {
//    @FXML private VBox root;
//    @FXML private Label welcomeText;
//    @FXML private Pane imagePane;
//
//    private boolean clicked;

    @FXML
    protected void onHelloButtonClick() {
        try {
            FXRouter.goTo("register");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

            try {
                FXRouter.goTo("major-staff");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }


}