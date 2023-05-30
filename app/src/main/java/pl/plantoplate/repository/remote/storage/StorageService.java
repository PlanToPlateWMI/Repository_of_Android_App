package pl.plantoplate.repository.remote.storage;

import java.util.ArrayList;

import pl.plantoplate.repository.models.Product;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StorageService {

    @GET("api/pantry/")
    Call<ArrayList<Product>> getStorage(@Header("Authorization") String token);

    @POST("api/pantry/")
    Call<ArrayList<Product>> addProductToStorage(@Header("Authorization") String token, @Body Product product);

    @DELETE("api/pantry/{id}")
    Call<ArrayList<Product>> deleteProductStorage(@Header("Authorization") String token, @Path("id") int productId);

    @POST("api/pantry/transfer")
    Call<ArrayList<Product>> transferBoughtProductsToStorage(@Header("Authorization") String token, @Body ArrayList<Integer> productIds);

    @PATCH("api/pantry/{id}")
    Call<ArrayList<Product>> changeProductAmountInStorage(@Header("Authorization") String token, @Path("id") int productId, @Body Product product);
}
