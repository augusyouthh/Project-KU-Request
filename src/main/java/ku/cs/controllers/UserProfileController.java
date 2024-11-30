package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class UserProfileController {
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;
    @FXML private Label idLabel;
    @FXML private Circle imagecircle;
    @FXML private Label errorLabel;
    @FXML private PasswordField oldPasswordTextfield;
    @FXML private PasswordField newPasswordTextfield;
    @FXML private PasswordField confirmPasswordTextfield;
    @FXML private Pane resetPasswordPane;
    @FXML private Button onChangeProfileImageButtonClick;
    @FXML private Label idLabel1;
    @FXML private Label majorLabel1;
    @FXML private Label majorLabel;
    @FXML private Label facultyLabel;
    @FXML private Label facultytextLabel;



    private UserList userList;
    private UserListFileDatasource datasource;
    private User user;

    @FXML
    public void initialize() {
        errorLabel.setText("");
        idLabel.setVisible(false);
        idLabel1.setVisible(false);
        majorLabel1.setVisible(false);
        majorLabel.setVisible(false);
        resetPasswordPane.setVisible(false);
        datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();
        Object data = FXRouter.getData();
        if (data instanceof User) {
            user = (User) data;
            updateUI(user);

        } else {
            usernameLabel.setText("Invalid user data");
        }
    }

    private User updateUser(User user) {
        userList = datasource.readData();
        User updatedUser = userList.findUserByUsername(user.getUsername());
        return updatedUser;
    }

    private void updateUI(User user) {
        if (user != null) {
            usernameLabel.setText(user.getUsername());
            roleLabel.setText(user.getRole());
            if (user.getRole().equals("admin")) {
                facultyLabel.setVisible(false);
                facultytextLabel.setVisible(false);
            }
            if (user.getRole().equals("majorStaff") || user.getRole().equals("student")) {
                majorLabel.setVisible(true);
                majorLabel1.setVisible(true);
                majorLabel.setText(user.getMajor());
                if (user.getRole().equals("advisor") || user.getRole().equals("student")) {
                    idLabel.setVisible(true);
                    idLabel1.setVisible(true);
                    idLabel.setText(user.getId());
                }
            }
            nameLabel.setText(user.getName());
            facultyLabel.setText(user.getFaculty());

            String imagePath = System.getProperty("user.dir") + File.separator + user.getProfilePicturePath();
            String url = new File(imagePath).toURI().toString();
            imagecircle.setFill(new ImagePattern(new Image(url)));
        }
    }
    @FXML
    private void onEnterButtonClick() {
        String oldPassword = oldPasswordTextfield.getText().trim();
        String newPassword = newPasswordTextfield.getText().trim();
        String confirmPassword = confirmPasswordTextfield.getText().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            errorLabel.setText("New password and confirmation do not match.");
            return;
        }
        User finduser = userList.findUserByUsername(user.getUsername());
        if (finduser == null || !BCrypt.checkpw(oldPassword, finduser.getPassword())) {
            errorLabel.setText("Username or old password is incorrect.");
            return;
        }

        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        finduser.setPassword(hashedNewPassword);
        datasource.writeData(userList);

        errorLabel.setText("Password successfully reset.");
        oldPasswordTextfield.clear();
        newPasswordTextfield.clear();
        confirmPasswordTextfield.clear();
    }


    @FXML
    private void onChangePasswordButtonClick() {
        resetPasswordPane.setVisible(!resetPasswordPane.isVisible());
    }
    @FXML
    private void onChangeProfileImageButtonClick() {
        // สร้าง FileChooser
        FileChooser fileChooser = new FileChooser();
        // เปลี่ยนไปใช้เส้นทางที่ตรงไปยัง data/userProfileImage
        File initialDirectory = new File("data/userProfileImage");
        if (!initialDirectory.exists()) {
            initialDirectory.mkdirs();
        }
        fileChooser.setInitialDirectory(initialDirectory);

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) onChangeProfileImageButtonClick.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                File destDir = new File("data"+File.separator+"userProfileImage");
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                String username = user.getUsername();

                File[] existingFiles = destDir.listFiles((dir, name) -> name.startsWith(username + "."));
                if (existingFiles != null) {
                    for (File existingFile : existingFiles) {
                        existingFile.delete(); // ลบไฟล์เดิม
                    }
                }

                String[] fileSplit = selectedFile.getName().split("\\.");
                String filename = user.getUsername() + "."+ fileSplit[fileSplit.length - 1].toLowerCase();
                Path target = FileSystems.getDefault().getPath(destDir.getAbsolutePath() + File.separator + filename);
                Files.copy(selectedFile.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                String profilePicPath = "data"+File.separator+"userProfileImage" + File.separator + filename;
                user.setProfilePicturePath(profilePicPath);
                User findUser = userList.findUserByUsername(user.getUsername());
                findUser.setProfilePicturePath(profilePicPath);
                datasource.writeData(userList);
                updateUI(updateUser(findUser));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("ไม่มีไฟล์ที่เลือก.");
        }
    }




    @FXML
    private void onBackButton() {
        try {
            navigateByRole(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void navigateByRole(User user) throws IOException {
        if (user == null) {
            throw new IOException("User is not logged in.");
        }

        switch (user.getRole()) {
            case "student":
                FXRouter.goTo("student", user);
                break;
            case "admin":
                FXRouter.goTo("main-admin", user);
                break;
            case "advisor":
                if (user.isFirstlogin()) {
                    System.out.println("Please change your password before your first login."); // Print a message instead of using a label
                    return;
                }
                FXRouter.goTo("main-advisor", user);
                break;
            case "facultyStaff":
                if (user.isFirstlogin()) {
                    System.out.println("Please change your password before your first login.");
                    return;
                }
                FXRouter.goTo("facultyStaff", user);
                break;
            case "majorStaff":
                if (user.isFirstlogin()) {
                    System.out.println("Please change your password before your first login.");
                    return;
                }
                FXRouter.goTo("departmentStaff", user);
                break;
            default:
                System.out.println("Invalid role. Please contact the administrator.");
                throw new IOException("Invalid role.");
        }
    }
}