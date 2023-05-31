package pl.plantoplate.repository.remote.shoppingList;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import pl.plantoplate.repository.models.Product;
import pl.plantoplate.repository.models.ShoppingList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShoppingListService {

    int maxAge = 3600 * 24 * 30;

    //@Headers("Cache-Control: max-age=" + maxAge)
    @GET("api/shopping")
    Call<ArrayList<Product>> getShoppingList(@Header("Authorization") String token, @Query("bought") boolean bought);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @POST("api/shopping")
    Call<ArrayList<Product>> addProductToShopList(@Header("Authorization") String token, @Body Product product);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @DELETE("api/shopping/{id}")
    Call<ArrayList<Product>> deleteProductFromShopList(@Header("Authorization") String token, @Path("id") int productId);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @PUT("api/shopping/{id}")
    Call<ShoppingList> changeProductStateInShopList(@Header("Authorization") String token, @Path("id") int productId);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @PATCH("api/shopping/{id}")
    Call<ArrayList<Product>> changeProductAmountInShopList(@Header("Authorization") String token, @Path("id") int productId, @Body Product product);
}
