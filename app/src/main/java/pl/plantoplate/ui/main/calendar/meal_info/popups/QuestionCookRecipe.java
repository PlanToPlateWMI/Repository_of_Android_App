package pl.plantoplate.ui.main.calendar.meal_info.popups;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.repository.MealRepository;
import pl.plantoplate.databinding.NewPopUpQuestionCookRecipeBinding;

/**
 * A dialog fragment that asks the user if he wants to cook the recipe.
 */
public class QuestionCookRecipe extends DialogFragment {

    private TextView acceptButton;
    private TextView cancelButton;
    private CompositeDisposable compositeDisposable;
    private SharedPreferences prefs;
    private View.OnClickListener listener;

    public QuestionCookRecipe() {
        listener = v -> {
        };
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewPopUpQuestionCookRecipeBinding binding = NewPopUpQuestionCookRecipeBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        compositeDisposable = new CompositeDisposable();
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        setupViews(binding);
        setClicklisteners();
        return dialog;
    }

    public void setupViews(NewPopUpQuestionCookRecipeBinding binding){
        acceptButton = binding.buttonYes;
        cancelButton = binding.buttonNo;
    }

    private void setClicklisteners() {
        acceptButton.setOnClickListener(v -> prepareMeal());

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Przepis nie został przygotowany", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    /**
     * Prepares a meal.
     */
    public void prepareMeal(){
        String token = "Bearer " + prefs.getString("token", "");
        MealRepository mealRepository = new MealRepository();
        Disposable disposable = mealRepository.prepareMeal(token, requireArguments().getInt("mealId"))
                .subscribe(message -> {
                    Toast.makeText(requireContext(), "Przepis został przygotowany i usunięty kalendarza", Toast.LENGTH_SHORT).show();
                    listener.onClick(getView());
                    dismiss();
                }, throwable -> Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
        compositeDisposable.add(disposable);
    }

    public void setOnAcceptClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}