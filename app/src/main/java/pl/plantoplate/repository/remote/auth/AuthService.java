package pl.plantoplate.repository.remote.auth;

import pl.plantoplate.repository.remote.models.CodeResponse;
import pl.plantoplate.repository.remote.models.JwtResponse;
import pl.plantoplate.repository.remote.models.Message;
import pl.plantoplate.repository.remote.models.UserRegisterData;
import pl.plantoplate.repository.remote.models.SignInData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {

    @POST("api/auth/signup")
    Call<CodeResponse> sendUserRegisterData(@Body UserRegisterData info);

    @GET("api/mail/code")
    Call<CodeResponse> getEmailConfirmCode(@Query("email") String email, @Query("type") String type);

    @POST("api/auth/signin")
    Call<JwtResponse> signinUser(@Body SignInData info);

    @POST("api/auth/password/reset")
    Call<Message> resetPassword(@Body SignInData info);
}
