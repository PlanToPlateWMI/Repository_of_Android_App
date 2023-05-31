package pl.plantoplate.repository.remote.storage;

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

public interface StorageService {

    int maxAge = 3600 * 24 * 30;

    //@Headers("Cache-Control: max-age=" + maxAge)
    @GET("api/pantry/")
    Call<ArrayList<Product>> getStorage(@Header("Authorization") String token);

    //Headers("Cache-Control: max-age=" + maxAge)
    @POST("api/pantry/")
    Call<ArrayList<Product>> addProductToStorage(@Header("Authorization") String token, @Body Product product);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @DELETE("api/pantry/{id}")
    Call<ArrayList<Product>> deleteProductStorage(@Header("Authorization") String token, @Path("id") int productId);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @POST("api/pantry/transfer")
    Call<ArrayList<Product>> transferBoughtProductsToStorage(@Header("Authorization") String token, @Body ArrayList<Integer> productIds);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @PATCH("api/pantry/{id}")
    Call<ArrayList<Product>> changeProductAmountInStorage(@Header("Authorization") String token, @Path("id") int productId, @Body Product product);
}
