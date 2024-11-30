package ku.cs.models;

public class Major {
    private String facultyId;
    private String majorName;
    private String majorId;

    public Major(String facultyId, String majorId, String majorName) {
        this.facultyId = facultyId;
        this.majorId = majorId;
        this.majorName = majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public String getMajorId() {
        return majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    @Override
    public String toString() {
        return "Major{" +
                "facultyId='" + facultyId + '\'' +
                ", majorName='" + majorName + '\'' +
                ", majorId='" + majorId + '\'' +
                '}';
    }
}
