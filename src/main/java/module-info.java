module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires java.desktop;
    opens ku.cs.cs211671project to javafx.fxml;
    exports ku.cs.cs211671project;
    exports ku.cs.controllers;
    opens ku.cs.controllers to javafx.fxml;
    exports ku.cs.models;
    opens ku.cs.models to javafx.base;
    exports ku.cs.controllers.admin;
    opens ku.cs.controllers.admin to javafx.fxml;
    exports ku.cs.controllers.advisor;
    opens ku.cs.controllers.advisor to javafx.fxml;
    exports ku.cs.controllers.student;
    opens ku.cs.controllers.student to javafx.fxml;
    exports ku.cs.controllers.majorStaff;
    opens ku.cs.controllers.majorStaff to javafx.fxml;
    exports ku.cs.controllers.components;
    opens ku.cs.controllers.components to javafx.fxml;

    exports ku.cs.controllers.facultyStaff;
    opens ku.cs.controllers.facultyStaff to javafx.fxml;

}