package pl.plantoplate.requests.shoppingList;

import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.BaseCallback;
import retrofit2.Callback;

public class GetShopListCallback extends BaseCallback implements Callback<ResponseBody> {

    private ShopListCallback callback;

    /**
     * Constructor to create a new CreateGroupCallback object.
     *
     * @param view The view object to display the Snackbar.
     */
    public GetShopListCallback(View view, ShopListCallback callback) {
        super(view);
        this.callback = callback;
    }

    @Override
    public void handleSuccessResponse(String response) {
        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>(){}.getType();
        ArrayList<Product> shoppingList = gson.fromJson(response, productListType);

        callback.setShoppingList(shoppingList);
    }

    @Override
    public void handleErrorResponse(int code) {
        switch (code) {
            case 400:
                break;
            case 403:
                break;
            case 404:
                break;
            case 500:
                break;
        }
    }
}
