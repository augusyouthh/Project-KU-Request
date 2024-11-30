package ku.cs.models;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;


public class Appeal {
    private String studentID;
    private String type; //ประเภท
    private String subject; // เรื่อง
    private String request; // มีความประสงค์คือ
    private LocalDate date; // วัน/เดือน/ปี ที่ส่ง
    private String studentSignature; // ลงนามนิสิต/ผู้ดำเนินการแทน
    private String majorEndorserSignature; // ลายเซ็นเจ้าหน้าที่ผู้อนุมัติ ภาค
    private String FacultyEndorserSignature; //ลายเซ็นเจ้าหน้าที่อนุมะติ คณะ
    private long second; //เก็บเวลาที่ส่ง
    private String status; //สถานะ
    private LocalTime sendtime; // เวลาที่ส่ง
    private String DeclineReason; //เหตุผลที่ปฎิเสธ
    private LocalDateTime DeclineDateTime; //วันเวลาที่ปฎิเสธ (ใช้ร่วมกัน)
    private LocalDate majorEndorserDate; //วันอนุมัติของ จนท ภาค
    private LocalDate FacultyEndorserDate; //วันอนุมัติของ จนท คณะ
    private String appealID;
    private String pathPDF;



    public Appeal(String studentID, String type , String subject, String request, LocalDate date, String studentSignature,
                  long second, String status, LocalTime sendtime, String DeclineReason, String majorEndorserSignature, LocalDate majorEndorserDate,
                  LocalDate FacultyEndorserDate, LocalDateTime DeclineDateTime, String FacultyEndorserSignature,String appealID, String pathPDF) {

        this.studentID = studentID;
        this.type = type;
        this.subject = subject;
        this.request = request;
        this.date = date;
        this.studentSignature = studentSignature;
        this.second = second;
        this.status = status;
        this.sendtime = sendtime;
        this.DeclineReason = DeclineReason;
        this.majorEndorserSignature = majorEndorserSignature;
        this.majorEndorserDate = majorEndorserDate;
        this.FacultyEndorserDate = FacultyEndorserDate;
        this.DeclineDateTime = DeclineDateTime;
        this.FacultyEndorserSignature = FacultyEndorserSignature;
        this.appealID = appealID;
        this.pathPDF = pathPDF;
    }

    public String getStudentID(){
        return studentID;
    }

    public String getType(){ return type; }

    public void setType(String type){this.type = type; }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStudentSignature() {
        return studentSignature;
    }

    public void setStudentSignature(String studentSignature) {
        this.studentSignature = studentSignature;
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(long second){ this.second = second;}

    public String getStatus(){ return status;}

    public void setStatus(String status){
        this.status = status;
    }

    public LocalTime getSendtime(){
        return sendtime;
    }

    public String getDeclineReason(){
        return DeclineReason;
    }
    public void setDeclineReason(String DeclineReason){
        this.DeclineReason = DeclineReason;
    }

    public String getMajorEndorserSignature() {
        return majorEndorserSignature;
    }

    public void setMajorEndorserSignature(String majorEndorserSignature){
        this.majorEndorserSignature = majorEndorserSignature;
    }
    public LocalDate getMajorEndorserDate(){
        return majorEndorserDate;
    }
    public void setMajorEndorserDate(LocalDate majorEndorserDate) {
        this.majorEndorserDate = majorEndorserDate;
    }

    public LocalDate getFacultyEndorserDate(){ return FacultyEndorserDate;}

    public void setFacultyEndorserDate(LocalDate FacultyEndorserDate){
        this.FacultyEndorserDate = FacultyEndorserDate;
    }

    public LocalDateTime getDeclineDateTime(){return DeclineDateTime;}

    public void setDeclineDateTime(LocalDateTime DeclineDateTime){
        this.DeclineDateTime = DeclineDateTime;
    }
    public void setDeclineDateTime() {
        this.DeclineDateTime = LocalDateTime.now();
    }

    public String getFacultyEndorserSignature() {
        return FacultyEndorserSignature;
    }

    public void setFacultyEndorserSignature(String facultyEndorserSignature) {
        FacultyEndorserSignature = facultyEndorserSignature;
    }

    public String getPathPDF() {
        return pathPDF;
    }

    public void setPathPDF(String pathPDF) {
        this.pathPDF = pathPDF;
    }

    public String getAppealID() {
        return appealID;
    }

    public void setAppealID(String appealID) {
        this.appealID = appealID;
    }

    @Override
    public String toString() {
        return "NormalAppeal{" +
                "type='" + type + '\'' +
                ", subject='" + subject + '\'' +
                ", request='" + request + '\'' +
                ", date=" + date + '\''+
                ", studentSignature='" + studentSignature + '\'' +
                ", second =" + second + '\'' + ",status =" + status +
                '}';
    }

}