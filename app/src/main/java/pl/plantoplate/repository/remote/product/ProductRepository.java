package pl.plantoplate.repository.remote.product;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private ProductService productService;

    //list of all active calls
    private ArrayList<Call<?>> calls;

    public ProductRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        productService = retrofitClient.getClient().create(ProductService.class);
        calls = new ArrayList<>();
    }

    public void getAllProducts(String token, ResponseCallback<ArrayList<Product>> callback) {
        Call<ArrayList<Product>> call = productService.getAllProducts(token, "all");
        calls.add(call);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {

                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Użytkownik o podanym adresie email nie istnieje.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void getOwnProducts(String token, ResponseCallback<ArrayList<Product>> callback) {
        Call<ArrayList<Product>> call = productService.getAllProducts(token, "group");
        calls.add(call);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {

                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Użytkownik o podanym adresie email nie istnieje.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void addProduct(String token, Product product, ResponseCallback<ArrayList<Product>> callback) {
        Call<ArrayList<Product>> call = productService.addProduct(token, product);
        calls.add(call);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Produkt o podanej nazwie już istnieje.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void modifyProduct(String token, int productId, Product product, ResponseCallback<ArrayList<Product>> callback) {
        Call<ArrayList<Product>> call = productService.changeProduct(token, productId, product);
        calls.add(call);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    System.out.println(code);
                    switch (code) {
                        case 400:
                            callback.onError("Produkt z takimi parametrami już istnieje.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
        }

    public void deleteProduct(String token, int productId, ResponseCallback<ArrayList<Product>> callback) {
        Call<ArrayList<Product>> call = productService.deleteProduct(token, productId);
        calls.add(call);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Próba usunięcia produktu z bazy wszytskich produktów.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void cancelCalls() {
        for (Call<?> call : calls) {
            call.cancel();
        }
    }
}

