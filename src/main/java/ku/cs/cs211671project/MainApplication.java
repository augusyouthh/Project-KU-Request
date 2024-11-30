package ku.cs.cs211671project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "CS211 Project", 1280, 720);
        configRoutes();
        FXRouter.goTo("first-page");
        //FXRouter.goTo("main-admin");
        //FXRouter.goTo("majorEndorser");
        //FXRouter.goTo("main-admin");
        //FXRouter.goTo("facultyStaff");
        //FXRouter.goTo("main-admin");
        //FXRouter.goTo("setting");

    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("hello", viewPath + "other/hello-view.fxml");
        FXRouter.when("first-page", viewPath + "other/first-page.fxml");
        FXRouter.when("my-team", viewPath + "other/my-team.fxml");
        FXRouter.when("setting", viewPath + "other/setting.fxml");


        FXRouter.when("login-page", viewPath + "login/login.fxml");
        FXRouter.when("register", viewPath + "login/register.fxml");
        FXRouter.when("resetPassword", viewPath + "login/resetpassword.fxml");

        //admin
        FXRouter.when("main-admin", viewPath + "admin/main-admin.fxml");
        FXRouter.when("faculty-data-admin", viewPath + "admin/faculty-data-admin.fxml");
        FXRouter.when("major-data-admin", viewPath + "admin/major-data-admin.fxml");
        FXRouter.when("edit-data-faculty", viewPath + "admin/edit-data-faculty.fxml");
        FXRouter.when("add-new-faculty", viewPath + "admin/addNewFacultyData.fxml");
        FXRouter.when("edit-data-major",  viewPath + "admin/edit-data-major.fxml");
        FXRouter.when("add-new-major", viewPath + "admin/add-new-major.fxml");
        FXRouter.when("dashboard", viewPath + "admin/dashboard.fxml");
        FXRouter.when("user-details", viewPath + "admin/user-detail-admin.fxml");
        FXRouter.when("add-staff", viewPath + "admin/add-staff.fxml");
        FXRouter.when("staff-table-admin", viewPath + "admin/staff-table-admin.fxml");
        FXRouter.when("staff-edit", viewPath + "admin/staffedit.fxml");


        //student
        FXRouter.when("student", viewPath + "student/main-student.fxml");
        FXRouter.when("student-appeal", viewPath + "student/student-appeal.fxml");
        FXRouter.when("normal-appeal", viewPath + "student/normal-appeal.fxml");
        FXRouter.when("appeal-tracking", viewPath + "student/appeal-list.fxml");
        FXRouter.when("user-profile", viewPath + "student/user-profile.fxml");
        FXRouter.when("leave-appeal", viewPath + "student/leave-appeal.fxml");
        FXRouter.when("enroll-appeal", viewPath + "student/enroll-appeal.fxml");
        FXRouter.when("appeal-detail", viewPath + "student/appeal-detail.fxml");


        //facultyStaff
        FXRouter.when("facultyStaff", viewPath + "facultyStaff/faculty-staff.fxml");
        FXRouter.when("facultyDetail", viewPath  + "facultyStaff/appeal-detail-facultyStaff.fxml");
        FXRouter.when("approve-faculty-staff", viewPath + "facultyStaff/approver-faculty.fxml");
        FXRouter.when("editApproveFacultyStaff", viewPath + "facultyStaff/edit-approve-faculty-staff.fxml");
        FXRouter.when("addApproveFacultyStaff", viewPath + "facultyStaff/add-approve-faculty-staff.fxml");


        //advisor
        FXRouter.when("advisor-approve", viewPath + "advisor/advisor-approve.fxml");
        FXRouter.when("main-advisor", viewPath + "advisor/main-advisor.fxml");
        FXRouter.when("advisor-appeal-page", viewPath + "advisor/advisor-appeal-page.fxml");


        //majorStaff
        FXRouter.when("student-in-major", viewPath + "majorStaff/student-in-major.fxml");
        FXRouter.when("departmentStaff", viewPath + "majorStaff/main-major-staff.fxml");
        FXRouter.when("majorEndorser", viewPath + "majorStaff/major-endorser-staff.fxml");
        FXRouter.when("add-major-endorser", viewPath + "majorStaff/major-endorser-staff.fxml");
        FXRouter.when("major-accept-appeal", viewPath + "majorStaff/major-accept-appeal.fxml");
        FXRouter.when("edit-major-endorser", viewPath+ "majorStaff/edit-approve-major-staff.fxml");
        FXRouter.when("student-detail-major-staff", viewPath + "majorStaff/student-detail-major-staff.fxml");
        FXRouter.when("add-student", viewPath + "majorStaff/add-student.fxml");
        FXRouter.when("approve-major-list", viewPath + "majorStaff/approve-major-staff.fxml");

    }




    public static void main(String[] args) {
        launch();
    }

}