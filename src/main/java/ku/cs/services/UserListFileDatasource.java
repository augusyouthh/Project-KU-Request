package ku.cs.services;

import ku.cs.models.UserList;
import ku.cs.models.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UserListFileDatasource implements Datasource<UserList> {

    private String directory;
    private String filename;

    public UserListFileDatasource(String directory, String filename) {
        this.directory = directory;
        this.filename = filename;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directory);
        if (!file.exists()) {
            file.mkdirs();
        }

        String filePath = directory + File.separator + filename;
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
    public UserList readData() {
        UserList userList = new UserList();
        String filePath = directory + File.separator + filename;
        File file = new File(filePath);
        boolean isFirstLine = true;

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            while ((line = buffer.readLine()) != null) {
                if (isFirstLine) { // ข้ามบรรทัดแรก
                    isFirstLine = false;
                    continue;
                }
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");

                String username = data[0].trim();
                String password = data[1].trim();
                String name = data[2].trim();
                LocalDate lastLoginDate = null;
                LocalTime lastLoginTime = null;

                if (data.length > 3 && !data[3].trim().isEmpty()) {
                    lastLoginDate = LocalDate.parse(data[3].trim(), dateFormatter);
                }

                if (data.length > 4 && !data[4].trim().isEmpty()) {
                    lastLoginTime = LocalTime.parse(data[4].trim(), timeFormatter);
                }
                String role = data.length > 5 ? data[5].trim() : null;
                String profilePicturePath = data[6].trim();
                boolean suspended = data.length > 7 ? Boolean.parseBoolean(data[8].trim()) : false;
                String faculty = data.length > 8 ? data[8].trim() : null;
                String major = data.length > 9 ? data[9].trim() : null;
                boolean firstlogin = data.length > 10 ? Boolean.parseBoolean(data[10].trim()) : false;
                String Id = data.length > 11 ? data[11].trim() : null;

                User user = new User(name, username, password, lastLoginDate, lastLoginTime, role, profilePicturePath,suspended,faculty,major,firstlogin,Id);
                userList.addUser(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return userList;
    }

    @Override
    public void writeData(UserList userList) {
        String filePath = directory + File.separator + filename;
        File file = new File(filePath);

        try (BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            buffer.write("Username,Password,Name,LastLoginDate,LastLoginTime,Role,ProfilePicturePath,Suspended,Faculty,Major,FirstLogin,ID");
            buffer.newLine();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            for (User user : userList.getUsers()) {
                String profilePicturePath = "/images/defaultProfilePicture.png";
                switch (user.getRole()) {
                    case "admin":
                        profilePicturePath = user.getProfilePicturePath() != null ? user.getProfilePicturePath() : "data" + File.separator + "userProfileImage" + File.separator + "adminDefaultPicture.png";
                        break;
                    case "advisor":
                        profilePicturePath = user.getProfilePicturePath() != null ? user.getProfilePicturePath() : "data" + File.separator + "userProfileImage" + File.separator +"advisorStaffDefaultPicture.png";
                        break;
                    case "student":
                        profilePicturePath = user.getProfilePicturePath() != null ? user.getProfilePicturePath() : "data" + File.separator + "userProfileImage" + File.separator +"studentDefaultPicture.png";
                        break;
                    case "facultyStaff":
                        profilePicturePath = user.getProfilePicturePath() != null ? user.getProfilePicturePath() : "data" + File.separator + "userProfileImage" + File.separator +"facultyStaffDefaultPicture.png";
                        break;
                    case "majorStaff":
                        profilePicturePath = user.getProfilePicturePath() != null ? user.getProfilePicturePath() : "data" + File.separator + "userProfileImage" + File.separator +"majorStaffDefaultPicture.png";
                        break;
                    default:
                        break;
                }

                String line = String.join(",",
                        user.getUsername(),
                        user.getPassword(),
                        user.getName(),
                        user.getLastLoginDate() != null ? user.getLastLoginDate().format(dateFormatter) : "",
                        user.getLastLoginTime() != null ? user.getLastLoginTime().format(timeFormatter) : "",
                        user.getRole(),
                        profilePicturePath,
                        String.valueOf(user.isSuspended()),
                        user.getFaculty(),
                        user.getMajor(),
                        String.valueOf(user.isFirstlogin()),
                        user.getId()
                );

                buffer.write(line);
                buffer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
