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

package pl.plantoplate.repository.local;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;

import pl.plantoplate.repository.local.dao.UserDao;
import pl.plantoplate.repository.local.models.User;

@Database(entities = {User.class}, version = 1)
public abstract class ApplicationDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static volatile ApplicationDatabase INSTANCE;

    public static ApplicationDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ApplicationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ApplicationDatabase.class, "plantoplate.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
