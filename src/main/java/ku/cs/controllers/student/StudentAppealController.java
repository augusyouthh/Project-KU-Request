package ku.cs.controllers.student;

import javafx.fxml.FXML;
import ku.cs.models.User;
import ku.cs.services.FXRouter;
import java.io.IOException;


public class StudentAppealController {
    private User user;


    @FXML
    private void initialize() {
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;

        }
    }

    @FXML
    public void onBackButtonClick() {
        try {
            FXRouter.goTo("student",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onNormalAppealClick() {
        try {
            FXRouter.goTo("normal-appeal", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onLeaveAppealClick() {
        try {
            FXRouter.goTo("leave-appeal",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onEnrollAppealClick() {
        try {
            FXRouter.goTo("enroll-appeal",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}