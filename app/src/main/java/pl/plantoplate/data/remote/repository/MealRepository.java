package pl.plantoplate.data.remote.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.models.Message;
import pl.plantoplate.data.remote.models.meal.MealPlanNew;
import pl.plantoplate.data.remote.models.meal.Meal;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.data.remote.models.recipe.RecipeInfo;
import pl.plantoplate.data.remote.service.MealService;

public class MealRepository {
    private final MealService mealService;

    public MealRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        mealService = retrofitClient.getClient().create(MealService.class);
    }

    public Single<List<Meal>> getMealsByDate(String token, LocalDate date) {
        String incorrectDateFormat = "Niepoprawny format daty. Poprawny format to: yyyy-MM-dd";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, incorrectDateFormat);

        return mealService.getMealsByDate(token, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Meal>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Message> planMeal(String token, MealPlan mealPlan) {
        String incorrectData = "Niepoprawne dane";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, incorrectData);

        return mealService.planMeal(token, mealPlan)
                .onErrorResumeNext(throwable -> new ErrorHandler<Message>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<RecipeInfo> getMealDetailsById(String token, int mealId) {
        String mealDoesNotBelongToGroup = "Posiłek o podanym id nie należy do tej grupy.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, mealDoesNotBelongToGroup);

        return mealService.getMealDetailsById(token, mealId)
                .onErrorResumeNext(throwable -> new ErrorHandler<RecipeInfo>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Message> deleteMealById(String token, int mealId) {
        String mealDoesNotBelongToGroup = "Posiłek o podanym id nie należy do tej grupy lub nie istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, mealDoesNotBelongToGroup);

        return mealService.deleteMealById(token, mealId)
                .onErrorResumeNext(throwable -> new ErrorHandler<Message>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Message> planMealV1(String token, MealPlanNew addMealProducts) {
        String incorrectData = "Niepoprawne dane";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, incorrectData);

        return mealService.planMealV1(token, addMealProducts)
                .onErrorResumeNext(throwable -> new ErrorHandler<Message>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Message> planMealV2(String token, MealPlanNew addMealProducts) {
        String incorrectData = "Niepoprawne dane";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, incorrectData);

        return mealService.planMealV2(token, addMealProducts)
                .onErrorResumeNext(throwable -> new ErrorHandler<Message>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Message> prepareMeal(String token, int mealId) {
        String mealDoesNotBelongToGroup = "Posiłek o podanym id nie należy do tej grupy lub nie istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, mealDoesNotBelongToGroup);

        return mealService.prepareMeal(token, mealId)
                .onErrorResumeNext(throwable -> new ErrorHandler<Message>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}