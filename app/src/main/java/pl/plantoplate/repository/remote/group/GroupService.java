package pl.plantoplate.repository.remote.group;

import pl.plantoplate.repository.remote.models.CodeResponse;
import pl.plantoplate.repository.remote.models.CreateGroupData;
import pl.plantoplate.repository.remote.models.JwtResponse;
import pl.plantoplate.repository.remote.models.UserJoinGroupData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GroupService {

    @POST("api/auth/group")
    Call<JwtResponse> createGroup(@Body CreateGroupData createGroupRequest);

    @POST("api/invite-codes")
    Call<JwtResponse> joinGroupByCode(@Body UserJoinGroupData userJoinGroupRequest);

    @GET("api/invite-codes")
    Call<CodeResponse> generateGroupCode(@Header("Authorization") String token, @Query("role") String role);
}
