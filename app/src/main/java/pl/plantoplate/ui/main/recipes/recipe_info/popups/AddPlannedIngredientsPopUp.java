package pl.plantoplate.ui.main.recipes.recipe_info.popups;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import pl.plantoplate.databinding.NewPopUpQuestionAddToShopingListBinding;

public class AddPlannedIngredientsPopUp extends DialogFragment {

    private TextView acceptButton;
    private TextView cancelButton;
    private View.OnClickListener listener;

    public AddPlannedIngredientsPopUp() {
        listener = v -> {
        };
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewPopUpQuestionAddToShopingListBinding binding = NewPopUpQuestionAddToShopingListBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);

        setupViews(binding);
        setClickListeners();
        return dialog;
    }

    public void setupViews(NewPopUpQuestionAddToShopingListBinding binding){
        acceptButton = binding.buttonYes;
        cancelButton = binding.buttonNo;
    }

    public void setClickListeners(){
        cancelButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Produkty nie zostały dodane do listy zakupów", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        acceptButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Dodawanie do listy zakupów...", Toast.LENGTH_SHORT).show();
            listener.onClick(v);
            dismiss();
        });
    }

    public void setOnAcceptButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
