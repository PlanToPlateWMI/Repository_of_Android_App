package pl.plantoplate.repository.remote;

public interface ResponseCallback<T> {
    void onSuccess(T response);
    void onError(String errorMessage);
    void onFailure(String failureMessage);
}
