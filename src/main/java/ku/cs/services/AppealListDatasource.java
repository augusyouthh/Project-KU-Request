package ku.cs.services;

import ku.cs.models.Appeal;
import ku.cs.models.AppealList;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AppealListDatasource implements Datasource<AppealList>{
    private String filePath;

    public AppealListDatasource(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public AppealList readData() {
        AppealList appealList = new AppealList();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();


            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 17) {
                    String studentID = parts[0];
                    String type = parts[1];
                    String subject = parts[2];
                    String request = parts[3];
                    LocalDate date = parseDate(parts[4]);
                    String studentSignature = parts[5];
                    long timestamp = Long.parseLong(parts[6]);
                    String status = parts[7];
                    LocalTime time = LocalTime.parse(parts[8]);
                    String DeclineReason = parts[9];
                    String majorEndorserSignature = parts[10];
                    LocalDate majorEndorserDate = parseDate((parts[11]));
                    LocalDate FacultyDate = parseDate((parts[12]));
                    LocalDateTime DeclineDatetime = parseLocalDateTime((parts[13]));
                    String FacultyEndorserSignature = parts[14];
                    String appealID = parts[15];
                    String pathPDF = parts[16];


                    Appeal appeal = new Appeal(studentID, type, subject, request, date, studentSignature, timestamp, status, time, DeclineReason, majorEndorserSignature, majorEndorserDate, FacultyDate, DeclineDatetime, FacultyEndorserSignature, appealID,pathPDF);
                    appealList.addAppeal(appeal);

                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the appeals from the CSV file.");
            e.printStackTrace();
        }
        return appealList;
    }


    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty() || dateStr.equals("\"\"")) {
            return null;
        }
        return LocalDate.parse(dateStr);
    }

    private LocalDateTime parseLocalDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty() || dateTimeStr.equals("\"\"")) {
            return null; // Return null for invalid date time
        }
        return LocalDateTime.parse(dateTimeStr);
    }


    @Override
    public void writeData(AppealList data) {
        List<Appeal> appeals = data.getsAppeals();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("studentID,Type,Subject,Request,Date,Signature,Timestamp,Status,Time,DeclineReason,majorEndorserSignature,majorEndorserDate");
            writer.newLine();
            for (Appeal appeal : appeals) {
                String declineReason = appeal.getDeclineReason();
                String majorEndorserSignature = appeal.getMajorEndorserSignature();
                String FacultyEndorserSignature = appeal.getFacultyEndorserSignature();
                LocalDate majorEndorserDate = appeal.getMajorEndorserDate();
                LocalDate FacultyEndorderDate = appeal.getFacultyEndorserDate();
                LocalDateTime DeclineDateTime = appeal.getDeclineDateTime();
                String majorEndorserDateString;
                String FacultyEndorserDateString;
                String DeclineDateTimeString;
                String pathPDF = appeal.getPathPDF();
                if (declineReason == null || declineReason.isEmpty())  {
                    declineReason = "\"\"";
                }
                if (majorEndorserSignature == null || majorEndorserSignature.isEmpty()){
                    majorEndorserSignature = "\"\"";
                }
                if(majorEndorserDate == null){
                    majorEndorserDateString = "\"\"";
                }else{
                    majorEndorserDateString = majorEndorserDate.toString();
                }
                if(FacultyEndorderDate == null){
                    FacultyEndorserDateString = "\"\"";
                }else{
                    FacultyEndorserDateString = FacultyEndorderDate.toString();
                }
                if(DeclineDateTime == null){
                    DeclineDateTimeString = "\"\"";
                }else{
                    DeclineDateTimeString = DeclineDateTime.toString();
                }
                if (FacultyEndorserSignature== null || FacultyEndorserSignature.isEmpty())  {
                    FacultyEndorserSignature = "\"\"";
                }
                if(pathPDF == null){
                    pathPDF = "\"\"";
                }

                writer.write(
                        appeal.getStudentID() + ","
                                + appeal.getType() + ","
                                + appeal.getSubject() + ","
                                + appeal.getRequest() + ","
                                + appeal.getDate() + ","
                                + appeal.getStudentSignature() + ","
                                + appeal.getSecond() + ","
                                + appeal.getStatus() + ","
                                + appeal.getSendtime() + ","
                                + declineReason + ","
                                + majorEndorserSignature + ","
                                + majorEndorserDateString + ","
                                + FacultyEndorserDateString + ","
                                + DeclineDateTimeString + ","
                                + FacultyEndorserSignature + ","
                                + appeal.getAppealID() + ","
                                + pathPDF);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing the appeals to the CSV file.");
            e.printStackTrace();
        }
    }
}