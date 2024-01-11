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

/**
 * Class that handles meals.
 */
public class MealRepository {
    private final MealService mealService;

    public MealRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        mealService = retrofitClient.getClient().create(MealService.class);
    }

    /**
     * Retrieves a list of meals for a specific date associated with the provided token.
     *
     * @param token The token used to authenticate the request.
     * @param date  The date for which meals are requested.
     * @return A {@link Single} emitting a {@link List} of {@link Meal} objects representing the meals for the given date.
     * @throws NullPointerException if {@code token} or {@code date} is {@code null}.
     *
     * @see Meal
     */

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

    /**
     * Plans a meal using the provided token and meal plan data.
     *
     * @param token     The token used to authenticate the request.
     * @param mealPlan  The data containing information required to plan a meal.
     * @return A {@link Single} emitting a {@link Message} object representing the result of the meal planning operation.
     * @throws NullPointerException if {@code token} or {@code mealPlan} is {@code null}.
     *
     * @see MealPlan
     * @see Message
     */

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

    /**
     * Retrieves details of a meal by its ID associated with the provided token.
     *
     * @param token   The token used to authenticate the request.
     * @param mealId  The ID of the meal for which details are requested.
     * @return A {@link Single} emitting a {@link RecipeInfo} object representing the details of the specified meal.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see RecipeInfo
     */

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

    /**
     * Deletes a meal by its ID associated with the provided token.
     *
     * @param token   The token used to authenticate the request.
     * @param mealId  The ID of the meal to be deleted.
     * @return A {@link Single} emitting a {@link Message} object representing the result of the meal deletion operation.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see Message
     */

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

    /**
     * Plans a meal using the provided token and meal plan data (version 1).
     *
     * @param token            The token used to authenticate the request.
     * @param addMealProducts  The data containing information required to plan a meal (version 1).
     * @return A {@link Single} emitting a {@link Message} object representing the result of the meal planning operation.
     * @throws NullPointerException if {@code token} or {@code addMealProducts} is {@code null}.
     *
     * @see MealPlanNew
     * @see Message
     */

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

    /**
     * Plans a meal using the provided token and meal plan data (version 2).
     *
     * @param token            The token used to authenticate the request.
     * @param addMealProducts  The data containing information required to plan a meal (version 2).
     * @return A {@link Single} emitting a {@link Message} object representing the result of the meal planning operation.
     * @throws NullPointerException if {@code token} or {@code addMealProducts} is {@code null}.
     *
     * @see MealPlanNew
     * @see Message
     */

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

    /**
     * Prepares a meal by its ID associated with the provided token.
     *
     * @param token   The token used to authenticate the request.
     * @param mealId  The ID of the meal to be prepared.
     * @return A {@link Single} emitting a {@link Message} object representing the result of the meal preparation operation.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see Message
     */

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