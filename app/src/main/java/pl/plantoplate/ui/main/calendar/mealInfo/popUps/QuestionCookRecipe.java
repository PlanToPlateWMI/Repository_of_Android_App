package pl.plantoplate.ui.main.calendar.mealInfo.popUps;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import pl.plantoplate.databinding.NewPopUpQuestionCookRecipeBinding;

public class QuestionCookRecipe extends DialogFragment {

    private TextView acceptButton;
    private TextView cancelButton;
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

        setupViews(binding);
        setClicklisteners();
        return dialog;
    }

    public void setupViews(NewPopUpQuestionCookRecipeBinding binding){
        acceptButton = binding.buttonYes;
        cancelButton = binding.buttonNo;
    }

    private void setClicklisteners() {
        acceptButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Przepis został przygotowany i usunięty kalendarza", Toast.LENGTH_SHORT).show();
            //listener.onClick(v);
            //usuwanie przepisu z kalendarza i - produkty ze spizarni
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Przepis nie został przygotowany", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    public void setOnAcceptButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}