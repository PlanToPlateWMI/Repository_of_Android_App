package pl.plantoplate.repository.remote.user;

import pl.plantoplate.repository.remote.models.UserInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;

public interface UserService {

    @PATCH("api/users/username")
    Call<UserInfo> changeUsername(@Header("Authorization") String token, @Body UserInfo username);

    @PATCH("api/users/email")
    Call<UserInfo> changeEmail(@Header("Authorization") String token, String email);

    @PATCH("api/users/password")
    Call<UserInfo> changePassword(@Header("Authorization") String token, String password);

    @GET("api/users")
    Call<UserInfo> getUserInfo(@Header("Authorization") String token);
}
