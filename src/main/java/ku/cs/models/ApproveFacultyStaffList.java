package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class ApproveFacultyStaffList {
    private ArrayList<ApproveFacultyStaff> approveFacultyStaffList;

    // Constructor สำหรับการสร้าง ApproveFacultyStaffList จาก List ที่มีอยู่
    public ApproveFacultyStaffList(List<ApproveFacultyStaff> approveFacultyStaffList) {
        this.approveFacultyStaffList = new ArrayList<>(approveFacultyStaffList);
    }

    public ApproveFacultyStaffList() {approveFacultyStaffList = new ArrayList<>();}


    public void addNewApproveFacultyStaff(String name, String role, String faculty, String position) {
        name = name.trim();
        role = role.trim();
        faculty = faculty.trim();
        position = position.trim();

        for (ApproveFacultyStaff approveFacultyStaff : approveFacultyStaffList) {
            if (approveFacultyStaff.getName().equalsIgnoreCase(name)){
                return;
            }
        }
        approveFacultyStaffList.add(new ApproveFacultyStaff(name, role, faculty, position));
    }


    public void addNewApproveFacultyStaff(String name, String role, String faculty) {
        name = name.trim();
        role = role.trim();
        faculty = faculty.trim();

        for (ApproveFacultyStaff approveFacultyStaff : approveFacultyStaffList) {
            if (approveFacultyStaff.getName().equalsIgnoreCase(name)){
                return;
            }
        }
        approveFacultyStaffList.add(new ApproveFacultyStaff(name, role, faculty));
    }

    public ArrayList<ApproveFacultyStaff> getApproveFacultyStaffList() {
        return approveFacultyStaffList;
    }
}
