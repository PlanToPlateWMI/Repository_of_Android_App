package pl.plantoplate.repository.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

import pl.plantoplate.repository.models.Product;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    ArrayList<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE isOwn = 1")
    ArrayList<Product> getProduct(String type);

    @Insert
    ArrayList<Product> addProduct(Product product);

    @Update
    ArrayList<Product> changeProduct(Product product);

    @Delete
    ArrayList<Product> deleteProduct(Product product);
}
