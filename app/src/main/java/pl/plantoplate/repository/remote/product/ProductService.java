package pl.plantoplate.repository.remote.product;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.models.Product;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {

    int maxAge = 3600 * 24 * 30;

    //@Headers("Cache-Control: max-age=" + maxAge)
    @GET("api/products")
    Call<ArrayList<Product>> getAllProducts(@Header("Authorization") String token, @Query("type") String type);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @POST("api/products")
    Call<ArrayList<Product>> addProduct(@Header("Authorization") String token, @Body Product product);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @PATCH("api/products/{id}")
    Call<ArrayList<Product>> changeProduct(@Header("Authorization") String token, @Path("id") int productId, @Body Product product);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @DELETE("api/products/{id}")
    Call<ArrayList<Product>> deleteProduct(@Header("Authorization") String token, @Path("id") int productId);
}
