package pl.plantoplate.ui.main.recipes.recipeInfo.newPopUps;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.meal.MealPlanNew;
import pl.plantoplate.data.remote.models.meal.MealType;
import pl.plantoplate.data.remote.repository.MealRepository;
import pl.plantoplate.databinding.NewTryPopUpCalendarStartBinding;
import pl.plantoplate.ui.customViews.RadioGridGroup;
import pl.plantoplate.ui.customViews.calendar.CalendarStyle;
import pl.plantoplate.ui.customViews.calendar.ShortCalendar;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import timber.log.Timber;

public class PopUpCalendarPlanningStart extends DialogFragment {

    private SharedPreferences prefs;
    private CompositeDisposable compositeDisposable;
    private TextView acceptButton;
    private TextView cancelButton;
    private RadioGridGroup radioGridGroup;
    private TextInputEditText numberOfPortions;
    private ImageView plusButton;
    private ImageView minusButton;
    private ShortCalendar shortCalendar;
    private CheckBox checkBox;
    private MealPlanNew addMealProducts;

    public PopUpCalendarPlanningStart() {
    }

    public PopUpCalendarPlanningStart(MealPlanNew addMealProducts) {
        this.addMealProducts = addMealProducts;
        addMealProducts.setDate(LocalDate.now().toString());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewTryPopUpCalendarStartBinding binding = NewTryPopUpCalendarStartBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        compositeDisposable = new CompositeDisposable();

        setupViews(binding);
        setClicklisteners();
        return dialog;
    }

    public void setupViews(NewTryPopUpCalendarStartBinding binding){
        acceptButton = binding.zatwierdzenie;
        cancelButton = binding.close;

        radioGridGroup = binding.radioGroupBaza;
        radioGridGroup.setCheckedRadioButtonById(R.id.sniadanie);
        numberOfPortions = binding.ilosc;

        //if this pop up is checked, class -> PopUpCalendarPlanningEnd -> new_try_pop_up_calendar_end.xml
        checkBox = binding.checkBox;

        plusButton = binding.plus;
        minusButton = binding.minus;

        numberOfPortions.setText("1");

        shortCalendar = new ShortCalendar(requireContext(),
                binding.kalendarzRecyclerView,
                CalendarStyle.BLUE);
    }

    public void setClicklisteners(){
        shortCalendar.setUpItemButtons(new SetupItemButtons() {
            @Override
            public void setupDateItemClick(View v, LocalDate date) {
                Timber.e("Date clicked: %s", date.toString());
                addMealProducts.setDate(date.toString());
            }
        });

        acceptButton.setOnClickListener(v -> {
            addMealProducts.setMealType(MealType.fromString(radioGridGroup.getCheckedRadioButton().getText().toString()));
            addMealProducts.setPortions(Integer.parseInt(Objects.requireNonNull(numberOfPortions.getText()).toString()));
            if(checkBox.isChecked()){
                addMealProducts.setIsProductsAdd(true);
                PopUpCalendarPlanningEnd popUpCalendarPlanningEnd = new PopUpCalendarPlanningEnd(addMealProducts);
                popUpCalendarPlanningEnd.show(getParentFragmentManager(), "PopUpCalendarPlanningEnd");
                dismiss();
            }
            else{
                planMeal();
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());

        plusButton.setOnClickListener(v -> {
            if (numberOfPortions.getText().toString().length() == 0) {
                numberOfPortions.setError("Podaj liczbę porcji");
                return;
            }
            int numberOfServingsInt = Integer.parseInt(Objects.requireNonNull(numberOfPortions.getText()).toString());
            numberOfServingsInt++;
            numberOfPortions.setText(String.valueOf(numberOfServingsInt));
        });

        minusButton.setOnClickListener(v -> {
            if (numberOfPortions.getText().toString().length() == 0) {
                numberOfPortions.setError("Podaj liczbę porcji");
                return;
            }
            int numberOfServingsInt = Integer.parseInt(Objects.requireNonNull(numberOfPortions.getText()).toString());
            numberOfServingsInt--;
            numberOfPortions.setText(String.valueOf(numberOfServingsInt));
        });

        numberOfPortions.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();

                if (!input.isEmpty()) {
                    try {
                        if (input.contains(".")) {
                            numberOfPortions.setText(input.replace(".", ""));
                        }

                        int portions = Integer.parseInt(s.toString());
                        if (portions < 1) {
                            numberOfPortions.setText("1");
                        }
                        if (portions > 10) {
                            numberOfPortions.setText("10");
                        }
                        numberOfPortions.setSelection(numberOfPortions.getText().length());
                    } catch (NumberFormatException ignored) {}
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    public void planMeal(){
        String token = "Bearer " + prefs.getString("token", "");
        MealRepository mealRepository = new MealRepository();
        Disposable disposable = mealRepository.planMealV2(token, addMealProducts)
                .subscribe(mealPlan -> {
                    Toast.makeText(requireContext(), "Przepis został dodany do kalendarza", Toast.LENGTH_SHORT).show();
                    dismiss();
                }, throwable -> Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}