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

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import pl.plantoplate.R;
import pl.plantoplate.databinding.NewTryPopUpCalendarStartBinding;
import pl.plantoplate.ui.customViews.RadioGridGroup;
import pl.plantoplate.ui.customViews.calendar.CalendarStyle;
import pl.plantoplate.ui.customViews.calendar.ShortCalendar;

public class PopUpCalendarPlanningStart extends DialogFragment {

    private SharedPreferences prefs;
    private CompositeDisposable compositeDisposable;
    private TextView acceptButton;
    private TextView cancelButton;
    private RadioGridGroup radioGridGroup;
    private TextInputEditText numberOfPortions;
    private View.OnClickListener listener;
    private ImageView plusButton;
    private ImageView minusButton;
    private ShortCalendar shortCalendar;
    private CheckBox checkBox;

    public PopUpCalendarPlanningStart() {
    }

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
        acceptButton = binding.close;
        cancelButton = binding.zatwierdzenie;

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
        acceptButton.setOnClickListener(v -> {
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            dismiss();
        });

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

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
        });

    }


}
