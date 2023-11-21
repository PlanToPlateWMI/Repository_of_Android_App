package pl.plantoplate.ui.main.calendar.mealInfo.popUps;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import pl.plantoplate.databinding.NewPopUpQuestionAddToCalendarBinding;
import pl.plantoplate.databinding.NewPopUpQuestionDeleteRecipeBinding;

public class QuestionDeleteRecipe extends DialogFragment {

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
            Toast.makeText(requireContext(), "Przepis został usunięty z kalendarza", Toast.LENGTH_SHORT).show();
            //listener.onClick(v);
            //usuwanie z kalendarza przepisu
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Przepis nie został usunięty z kalendarza", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    public void setOnAcceptButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}