package ku.cs.services;

import ku.cs.models.ApproveFacultyStaff;
import ku.cs.models.ApproveFacultyStaffList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ApproveFacultyStaffListDatasource implements Datasource<ApproveFacultyStaffList>{
    private String directoryName;
    private String fileName;

    public ApproveFacultyStaffListDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
    }

    // ตรวจสอบว่ามีไฟล์ให้อ่านหรือไม่ ถ้าไม่มีให้สร้างไฟล์เปล่า
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
    public ApproveFacultyStaffList readData() {
        ApproveFacultyStaffList approveFacultyStaffList = new ApproveFacultyStaffList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        // เตรียม object ที่ใช้ในการอ่านไฟล์
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);
        String line = "";
        try{
            while((line = buffer.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length == 3){
                    String nameId = data[0].trim();
                    String roleId = data[1].trim();
                    String facultyId = data[2].trim();
                    approveFacultyStaffList.addNewApproveFacultyStaff(nameId, roleId, facultyId);
                }
                else {
                    System.out.println("invalid line: " + line);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }

        return approveFacultyStaffList;
    }

    @Override
    public void writeData(ApproveFacultyStaffList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการเขียนไฟล์
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                fileOutputStream,
                StandardCharsets.UTF_8
        );
        BufferedWriter buffer = new BufferedWriter(outputStreamWriter);
        try{
            for (ApproveFacultyStaff approveFacultyStaff : data.getApproveFacultyStaffList()){
                if (approveFacultyStaff.getPosition() == null){
                    String line = approveFacultyStaff.getName() + "," + approveFacultyStaff.getRole() + "," + approveFacultyStaff.getFaculty();
                    buffer.append(line);
                    buffer.append("\n");
                }
                else {
                    String line = approveFacultyStaff.getName() + "," + approveFacultyStaff.getRole() + "ฝ่าย" + approveFacultyStaff.getPosition() + "," + approveFacultyStaff.getFaculty();
                    buffer.append(line);
                    buffer.append("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try{
                buffer.flush();
                buffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
