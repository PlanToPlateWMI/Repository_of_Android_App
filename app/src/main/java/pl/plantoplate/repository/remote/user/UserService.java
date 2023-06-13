package pl.plantoplate.repository.remote.user;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.models.JwtResponse;
import pl.plantoplate.repository.remote.models.Message;
import pl.plantoplate.repository.remote.models.UserInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Query;

public interface UserService {

    @PATCH("api/users/username")
    Call<UserInfo> changeUsername(@Header("Authorization") String token, @Body UserInfo username);

    @PATCH("api/users/email")
    Call<JwtResponse> changeEmail(@Header("Authorization") String token, @Body UserInfo userInfo);

    @PATCH("api/users/password")
    Call<UserInfo> changePassword(@Header("Authorization") String token, @Body UserInfo password);

    @GET("api/users")
    Call<UserInfo> getUserInfo(@Header("Authorization") String token);

    @GET("api/users/password/match")
    Call<Message> validatePasswordMatch(@Header("Authorization") String token, @Query("password") String password);

    @GET("api/users/infos")
    Call<ArrayList<UserInfo>> getUsersInfo(@Header("Authorization") String token);

    @PATCH("api/users/roles")
    Call<ArrayList<UserInfo>> changePermissions(@Header("Authorization") String token, @Body ArrayList<UserInfo> usersInfo);
}
