package pl.plantoplate.repository.local;

import androidx.room.RoomDatabase;
import androidx.room.Database;
import pl.plantoplate.repository.models.Product;

@Database(entities = {Product.class}, version = 1)
public abstract class ApplicationDatabase extends RoomDatabase {

}
