package pl.plantoplate.ui.main.recipes.recipe_info.popups;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import pl.plantoplate.databinding.NewPopUpQuestionAddToCalendarBinding;

public class QuestionAddToCalendarPopUp extends DialogFragment {

    private TextView acceptButton;
    private TextView cancelButton;
    private View.OnClickListener listener;

    public QuestionAddToCalendarPopUp() {
        listener = v -> {
        };
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewPopUpQuestionAddToCalendarBinding binding = NewPopUpQuestionAddToCalendarBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);

        setupViews(binding);
        setClicklisteners();
        return dialog;
    }

    public void setupViews(NewPopUpQuestionAddToCalendarBinding binding){
        acceptButton = binding.buttonYes;
        cancelButton = binding.buttonNo;
    }

    private void setClicklisteners() {
        acceptButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Trwa planowanie...", Toast.LENGTH_SHORT).show();
            listener.onClick(v);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Przepis nie został dodany do kalendarza", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    public void setOnAcceptButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
