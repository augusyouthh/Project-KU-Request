package ku.cs.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

import ku.cs.services.LastLoginComparator;
import org.mindrot.jbcrypt.BCrypt;
public class UserList {
    private ArrayList<User> users;

    public UserList() {
        users = new ArrayList<>();
    }


    public void addUser(User user) {
        users.add(user);
    }


    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void updateLastLogin(String username) {
        User user = findUserByUsername(username);
        if (user != null) {
            LocalDate nowDate = LocalDate.now();
            LocalTime nowTime = LocalTime.now();
            user.setLastLoginDate(nowDate);
            user.setLastLoginTime(nowTime);
        }
    }

    public User authenticate(String username, String password) {
        User user = findUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            updateLastLogin(username);
            return user;
        }

        return null;
    }
    public User findUserByName(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public User findUserById(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }




    public void sortUsersByLastLogin() {
        Collections.sort(users, new LastLoginComparator());
    }
    public ArrayList<User> getUsers() {
        return users;
    }
}
