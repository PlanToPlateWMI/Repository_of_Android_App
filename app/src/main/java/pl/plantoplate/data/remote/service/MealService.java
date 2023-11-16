package pl.plantoplate.data.remote.service;

import java.util.ArrayList;
import io.reactivex.rxjava3.core.Single;
import pl.plantoplate.data.remote.models.meal.Meal;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface MealService {

    @GET("api/meals")
    Single<ArrayList<Meal>> getMealsByDate(@Header("Authorization") String token, @Query("date") String date);
}
