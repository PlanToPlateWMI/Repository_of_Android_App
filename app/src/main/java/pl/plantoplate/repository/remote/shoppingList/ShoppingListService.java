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
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShoppingListService {

    @GET("api/shopping")
    Call<ArrayList<Product>> getShoppingList(@Header("Authorization") String token, @Query("bought") boolean bought);

    @POST("api/shopping")
    Call<ArrayList<Product>> addProductToShopList(@Header("Authorization") String token, @Body Product product);

    @DELETE("api/shopping/{id}")
    Call<ArrayList<Product>> deleteProductFromShopList(@Header("Authorization") String token, @Path("id") int productId);

    @PUT("api/shopping/{id}")
    Call<ShoppingList> changeProductStateInShopList(@Header("Authorization") String token, @Path("id") int productId);

    @PATCH("api/shopping/{id}")
    Call<ArrayList<Product>> changeProductAmountInShopList(@Header("Authorization") String token, @Path("id") int productId, @Body Product product);
}
