/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
