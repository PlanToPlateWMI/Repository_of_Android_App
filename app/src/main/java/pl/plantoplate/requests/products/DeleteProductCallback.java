package pl.plantoplate.requests.products;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.BaseCallback;

public class DeleteProductCallback extends BaseCallback implements retrofit2.Callback<ResponseBody> {


    /**
     * Constructor to create a new CreateGroupCallback object.
     *
     * @param view The view object to display the Snackbar.
     */
    public DeleteProductCallback(View view) {
        super(view);
    }


    @Override
    public void handleSuccessResponse(String response) {
        Snackbar.make(view, "Produkt został usunięty!", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void handleErrorResponse(int code) {
        switch (code) {
            case 401:
                Snackbar.make(view, "Nieautoryzowany dostęp!", Snackbar.LENGTH_LONG).show();
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
