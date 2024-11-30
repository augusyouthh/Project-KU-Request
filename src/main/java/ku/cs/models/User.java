package ku.cs.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class User implements Comparable<User> {
    protected String name;
    protected String username;
    protected String password;
    protected LocalDate lastLoginDate;
    protected LocalTime lastLoginTime;
    protected String role;
    protected String profilePicturePath;
    protected boolean suspend;
    protected String faculty;
    protected String major;
    protected boolean firstlogin;
    protected String Id;

    public User(String name, String username, String password, LocalDate lastLoginDate, LocalTime lastLoginTime, String role, String profilePicturePath, boolean banned, String faculty, String major, boolean firstlogin, String Id) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.lastLoginDate = lastLoginDate;
        this.lastLoginTime = lastLoginTime;
        this.role = role;
        this.profilePicturePath = profilePicturePath;
        this.suspend = banned;
        this.faculty = faculty;
        this.major = major;
        this.firstlogin = firstlogin;
        this.Id = Id;
    }

    public User(String name, String username, String password, String role, String profilePicturePath,String id) {
        this(name , username, password, null, null, role, profilePicturePath, false, null, null,false, id);
    }
    public User(String name, String username, String password, String role, String profilePicturePath,String id,String faculty,String major) {
        this(name , username, password, null, null, role, profilePicturePath, false, faculty, major,false, id);
    }
    public User(String name, String username, String password, String role, String id, String faculty, String major) {
        this(name,username,password,role,null,id,faculty,major);
    }




    public boolean isSuspended() {
        return suspend;
    }

    public void setSuspended(boolean suspended) {
        this.suspend = suspended;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMajor() {
        return major;
    }

    public boolean isSuspend() {
        return suspend;
    }

    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
    }

    public boolean isFirstlogin() {
        return firstlogin;
    }

    public void setFirstlogin(boolean firstlogin) {
        this.firstlogin = firstlogin;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getLastLoginDate() {
        return lastLoginDate;
    }

    public LocalTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public void setLastLoginTime(LocalTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public boolean check(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public int compareTo(User other) {
        int dateComparison = other.lastLoginDate.compareTo(this.lastLoginDate);
        if (dateComparison != 0) {
            return dateComparison;
        }
        return other.lastLoginTime.compareTo(this.lastLoginTime);
    }
}