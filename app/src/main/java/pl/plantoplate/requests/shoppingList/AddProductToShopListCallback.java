package pl.plantoplate.requests.shoppingList;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.BaseCallback;
import retrofit2.Callback;

public class AddProductToShopListCallback extends BaseCallback implements Callback<ResponseBody> {

    /**
     * Constructor to create a new CreateGroupCallback object.
     *
     * @param view The view object to display the Snackbar.
     */
    public AddProductToShopListCallback(View view) {
        super(view);
    }

    /**
     * Handles the API server success responses.
     *
     * @param response The response object.
     */
    @Override
    public void handleSuccessResponse(String response) {
        Snackbar.make(view, "Dodano produkt do listy zakupów!", Snackbar.LENGTH_LONG).show();
    }

    /**
     * Handles the API server error responses.
     *
     * @param code The error code.
     */
    @Override
    public void handleErrorResponse(int code) {
        switch (code) {
            case 400:
                Snackbar.make(view, "Użytkownik próbuje dodać produkt nie z tej grupy lub ilość jest ujemna lub 0", Snackbar.LENGTH_LONG).show();
                break;
            case 500:
                Snackbar.make(view, "Błąd serwera!", Snackbar.LENGTH_LONG).show();
                break;
            default:
                Snackbar.make(view, "Nieznana odpowiedź serwera!", Snackbar.LENGTH_LONG).show();
                break;
        }
    }
}
