package pl.plantoplate.repository.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import pl.plantoplate.repository.local.models.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE active = 1")
    User getCurrentUser();

    @Query("SELECT email FROM users WHERE active = 1")
    String getCurrentUserName();

    @Query("SELECT password FROM users WHERE active = 1")
    String getCurrentUserEmail();

    @Query("SELECT token FROM users WHERE active = 1")
    String getCurrentUserPassword();

    @Query("SELECT token FROM users WHERE active = 1")
    String getCurrentUserToken();

    @Query("SELECT role FROM users WHERE active = 1")
    String getCurrentUserRole();

    @Insert
    void addUser(User user);

    @Query("DELETE FROM users WHERE active = 1")
    void deleteCurrentUser();

    @Update
    void updateCurrentUser(User user);

    @Query("UPDATE users SET name = :name WHERE active = 1")
    void updateCurrentUserName(String name);

    @Query("UPDATE users SET email = :email WHERE active = 1")
    void updateCurrentUserEmail(String email);

    @Query("UPDATE users SET password = :password WHERE active = 1")
    void updateCurrentUserPassword(String password);

    @Query("UPDATE users SET password = :token WHERE active = 1")
    void updateCurrentUserToken(String token);

    @Query("UPDATE users SET role = :role WHERE active = 1")
    void updateCurrentUserRole(String role);
}
