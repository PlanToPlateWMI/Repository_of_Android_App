package pl.plantoplate.data.remote;

import java.net.UnknownHostException;
import java.util.HashMap;

import io.reactivex.rxjava3.core.Single;
import retrofit2.HttpException;

public class ErrorHandler<T> {

    public Single<T> handleHttpError(Throwable throwable, HashMap<Integer, String> errorMessages) {
        String errorMessage;

        if (throwable instanceof UnknownHostException) {
            errorMessage = "Problemy z siecią. Sprawdź swoje połączenie.";
        } else if (throwable instanceof HttpException) {
            errorMessage = errorMessages.get(((HttpException) throwable).code());
        } else {
            errorMessage = "Wystąpił nieznany błąd.";
        }
        return Single.error(new Exception(errorMessage, throwable));
    }
}