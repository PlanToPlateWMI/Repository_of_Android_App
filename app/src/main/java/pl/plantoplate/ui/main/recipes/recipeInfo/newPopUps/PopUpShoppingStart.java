package pl.plantoplate.ui.main.recipes.recipeInfo.newPopUps;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import pl.plantoplate.R;
import pl.plantoplate.databinding.NewTryPopUpShoppingStartBinding;
import pl.plantoplate.ui.customViews.calendar.CalendarStyle;
import pl.plantoplate.ui.customViews.calendar.ShortCalendar;

public class PopUpShoppingStart extends DialogFragment {

    private CompositeDisposable compositeDisposable;
    private SharedPreferences prefs;

    private TextView acceptButton;
    private TextView cancelButton;

    private ImageView plusButton;
    private ImageView minusButton;

    private TextInputEditText numberOfPortions;
    private View.OnClickListener listener;

    private CheckBox checkBoxSynch;
    private CheckBox checkBoxPlanning;

    public PopUpShoppingStart() {
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewTryPopUpShoppingStartBinding binding = NewTryPopUpShoppingStartBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        compositeDisposable = new CompositeDisposable();

        setupViews(binding);
        //setClicklisteners();
        return dialog;
    }

    public void setupViews(NewTryPopUpShoppingStartBinding binding){
        acceptButton = binding.close;
        cancelButton = binding.zatwierdzenie;

        numberOfPortions = binding.ilosc;

        //if this pop up is checked, class -> PopUpShoppingEnd-> new_try_pop_up_planning_end.xml
        checkBoxPlanning = binding.checkBox;

        //have synchro ONLY if planning is checked!!!!!!!!
        checkBoxSynch = binding.checkBoxSynchro;

        plusButton = binding.plus;
        minusButton = binding.minus;

        numberOfPortions.setText("1");
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
