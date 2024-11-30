package ku.cs.services;

import java.io.*;
import java.nio.charset.StandardCharsets;
import ku.cs.models.Student;
import ku.cs.models.StudentList;

public class PreRegisterStudentListFileDatasource implements Datasource<StudentList> {
    private String directoryName;
    private String fileName;

    public PreRegisterStudentListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public StudentList readData() {
        StudentList studentList = new StudentList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        boolean isFirstLine = true;

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;

            while ((line = buffer.readLine()) != null) {
                if (isFirstLine) { // ข้ามบรรทัดแรก
                    isFirstLine = false;
                    continue;
                }
                if (line.isEmpty()) continue;

                String[] data = line.split(",");

                // Check if we have at least the first 6 columns
                if (data.length >= 6) {
                    String name = data[0].trim();
                    String id = data[1].trim();
                    String email = data[2].trim();
                    String faculty = data[3].trim();
                    String major = data[4].trim();
                    String advisorID = data.length > 5 ? data[5].trim() : null;

                    Student student = new Student(name,id, email, faculty, major,advisorID);
                    studentList.addStudent(student);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return studentList;
    }

    @Override
    public void writeData(StudentList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            buffer.write("Name,Id,Email,Faculty,Major,AdvisorID");
            buffer.newLine();
            for (Student student : data.getStudents()) {
                String line = String.join(",",
                        student.getName(),
                        student.getId(),
                        student.getEmail(),
                        student.getFaculty(),
                        student.getMajor(),
                        student.getAdvisorID()
                );
                buffer.write(line);
                buffer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


