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
