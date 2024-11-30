package ku.cs.models;

public class ApproveFacultyStaff {
    String name;
    String position;
    String faculty;
    String role;

    public ApproveFacultyStaff(String name, String role, String faculty) {
        this.name = name;
        this.role = role;
        this.faculty = faculty;
    }
    public ApproveFacultyStaff(String name, String role, String faculty, String position) {
        this.name = name;
        this.role = role;
        this.faculty = faculty;
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setFaculty(String faculty) {this.faculty = faculty;}

    public void setRole(String role) {this.role = role;}

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getFaculty() {return faculty;}

    public String getRole() {return role;}

    @Override
    public String toString() {
        return "ApproveFacultyStaff{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", faculty='" + faculty + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
