package ku.cs.models;

import java.util.ArrayList;

public class FacultyList {
    private ArrayList<Faculty> faculties;

    public FacultyList() {faculties = new ArrayList<>();}

    public void addNewFaculty(String facultyId, String facultyName){
        facultyName = facultyName.trim();
        facultyId = facultyId.trim();

        for (Faculty faculty1 : faculties){
            if (faculty1.getFacultyName().equalsIgnoreCase(facultyName)){
                return;
            }
        }
        faculties.add(new Faculty(facultyId.trim(), facultyName.trim()));
    }

    public void addNewMajor(String major, String majorId){
        major = major.trim();
        majorId = majorId.trim();


    }

    public ArrayList<Faculty> getFaculties() {
        return faculties;
    }
}
