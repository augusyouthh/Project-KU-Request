package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class MajorList {
    private ArrayList<Major> majors;

    public MajorList() {
        majors = new ArrayList<>();
    }

    public void addNewMajor(String facultyId, String majorId, String majorName) {
        facultyId = facultyId.trim();
        majorId = majorId.trim();
        majorName = majorName.trim();

        for (Major major : majors) {
            if (major.getMajorName().equalsIgnoreCase(majorName)) {
                return;
            }
        }
        majors.add(new Major(facultyId, majorId, majorName));
    }


    public ArrayList<String> getMajorsByFacultyId(String facultyId) {
        ArrayList<String> majorNames = new ArrayList<>();
        for (Major major : majors) {
            if (major.getFacultyId().equals(facultyId)) {
                majorNames.add(major.getMajorName());
            }
        }
        return majorNames;
    }
    public List<Major> filterMajorsByFaculty(String facultyId) {
        List<Major> filteredMajors = new ArrayList<>();
        for (Major major : majors) {
            if (major.getFacultyId().equals(facultyId)) {
                filteredMajors.add(major);
            }
        }
        return filteredMajors;
    }


    public ArrayList<Major> getMajors() {
        return majors;
    }
}

