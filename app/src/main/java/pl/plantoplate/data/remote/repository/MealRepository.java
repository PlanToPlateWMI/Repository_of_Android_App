package pl.plantoplate.data.remote.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.models.meal.Meal;
import pl.plantoplate.data.remote.service.MealService;

public class MealRepository {
    private final MealService mealService;

    public MealRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        mealService = retrofitClient.getClient().create(MealService.class);
    }

    public Single<ArrayList<Meal>> getMealsByDate(String token, LocalDate date) {
        return mealService.getMealsByDate(token, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Meal>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Niepoprawny format daty. Poprawny format to: yyyy-MM-dd");
                            put(500, "Wystąpił nieznany błąd.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}