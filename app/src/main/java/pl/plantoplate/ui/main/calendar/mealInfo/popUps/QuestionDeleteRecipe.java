package pl.plantoplate.ui.main.calendar.mealInfo.popUps;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.shoppingList.MealShopPlan;
import pl.plantoplate.data.remote.repository.MealRepository;
import pl.plantoplate.data.remote.repository.ShoppingListRepository;
import pl.plantoplate.databinding.NewPopUpQuestionAddToCalendarBinding;
import pl.plantoplate.databinding.NewPopUpQuestionDeleteRecipeBinding;

public class QuestionDeleteRecipe extends DialogFragment {

    private CompositeDisposable compositeDisposable;
    private TextView acceptButton;
    private TextView cancelButton;
    private View.OnClickListener listener;

    public QuestionDeleteRecipe() {
        listener = v -> {
        };
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewPopUpQuestionDeleteRecipeBinding binding = NewPopUpQuestionDeleteRecipeBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        compositeDisposable = new CompositeDisposable();

        setupViews(binding);
        setClicklisteners();
        return dialog;
    }

    public void setupViews(NewPopUpQuestionDeleteRecipeBinding binding){
        acceptButton = binding.buttonYes;
        cancelButton = binding.buttonNo;
    }

    private void setClicklisteners() {
        acceptButton.setOnClickListener(v -> {
            deleteMealFromPlanned(v, requireArguments().getInt("mealId"));
        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Przepis nie został usunięty z kalendarza", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    public void deleteMealFromPlanned(View v, int mealId) {
        String token = "Bearer " + requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("token", "");
        MealRepository mealRepository = new MealRepository();
        Disposable disposable = mealRepository.deleteMealById(token, mealId)
                .subscribe(
                        response -> {
                            Toast.makeText(requireContext(), "Przepis został usunięty z kalendarza", Toast.LENGTH_SHORT).show();
                            listener.onClick(v);
                            dismiss();
                        },
                        error -> Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

        compositeDisposable.add(disposable);

    }

    public void setOnAcceptButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}