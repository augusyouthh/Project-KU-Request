package ku.cs.models;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppealList {
    private ArrayList<Appeal> appeals;

    public AppealList(){
        this.appeals = new ArrayList<>();
    }

    public void addAppeal(Appeal appeal){
        appeals.add(appeal);
    }


    public List<Appeal> getsAppeals(){
        return new ArrayList<>(appeals);
    }

    public AppealList findAppealByStudentID(StudentList studentlist) {
        AppealList appealList1 = new AppealList();
        for (Appeal appeal : appeals) {
            for (Student student : studentlist.getStudents()) {
                if(appeal.getStudentID().equalsIgnoreCase(student.getId())) {
                    appealList1.addAppeal(appeal);
                }
            }
        }
        return appealList1;
    }





}