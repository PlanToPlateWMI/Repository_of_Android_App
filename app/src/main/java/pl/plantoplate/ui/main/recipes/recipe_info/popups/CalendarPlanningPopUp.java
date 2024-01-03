package pl.plantoplate.ui.main.recipes.recipe_info.popups;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import java.time.LocalDate;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.data.remote.models.meal.MealType;
import pl.plantoplate.data.remote.repository.MealRepository;
import pl.plantoplate.databinding.NewPopUpPlaningCalendarBinding;
import pl.plantoplate.ui.custom_views.RadioGridGroup;
import pl.plantoplate.ui.custom_views.calendar.CalendarStyle;
import pl.plantoplate.ui.custom_views.calendar.ShortCalendar;
import pl.plantoplate.ui.main.recycler_views.listeners.SetupItemButtons;
import timber.log.Timber;

public class CalendarPlanningPopUp extends DialogFragment {

    private MealPlan mealPlan;
    private TextView acceptButton;
    private TextView cancelButton;
    private RadioGridGroup radioGridGroup;
    private View.OnClickListener listener;
    private ShortCalendar shortCalendar;
    private SharedPreferences prefs;
    private CompositeDisposable compositeDisposable;

    public CalendarPlanningPopUp() {
    }

    public CalendarPlanningPopUp(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
        mealPlan.setDate(LocalDate.now().toString());
        listener = v -> {
        };
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewPopUpPlaningCalendarBinding binding = NewPopUpPlaningCalendarBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        compositeDisposable = new CompositeDisposable();

        setupViews(binding);
        setClicklisteners();
        return dialog;
    }

    public void setupViews(NewPopUpPlaningCalendarBinding binding){
        acceptButton = binding.buttonYes;
        cancelButton = binding.buttonNo;
        radioGridGroup = binding.radioGroupBaza;
        radioGridGroup.setCheckedRadioButtonById(R.id.sniadanie);
        shortCalendar = new ShortCalendar(requireContext(),
                binding.kalendarzRecyclerView,
                CalendarStyle.BLUE);
    }

    private void setClicklisteners() {
        shortCalendar.setUpItemButtons(new SetupItemButtons() {
            @Override
            public void setupDateItemClick(View v, LocalDate date) {
                Timber.e("Date clicked: %s", date.toString());
                mealPlan.setDate(date.toString());
            }
        });

        acceptButton.setOnClickListener(v -> {
            mealPlan.setMealType(MealType.fromString(radioGridGroup.getCheckedRadioButton().getText().toString()));
            planMeal(v);
        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Przepis nie został dodany do kalendarza", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    public void planMeal(View v){
        String token = "Bearer " + prefs.getString("token", "");
        MealRepository mealRepository = new MealRepository();
        Disposable disposable = mealRepository.planMeal(token, mealPlan)
                .subscribe(plan -> {
                    listener.onClick(v);
                    Toast.makeText(requireContext(), "Przepis został dodany do kalendarza", Toast.LENGTH_SHORT).show();
                    dismiss();
                }, throwable ->
                        Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
        compositeDisposable.add(disposable);
    }

    public void setOnAcceptButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public MealPlan getMealPlan() {
        return mealPlan;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
