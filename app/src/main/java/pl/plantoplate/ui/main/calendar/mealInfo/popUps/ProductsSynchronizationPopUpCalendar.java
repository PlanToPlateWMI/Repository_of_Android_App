package pl.plantoplate.ui.main.calendar.mealInfo.popUps;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.data.remote.models.shoppingList.MealShopPlan;
import pl.plantoplate.data.remote.repository.ShoppingListRepository;
import pl.plantoplate.databinding.NewPopUpQuestionSynhronizationOnBinding;
import timber.log.Timber;

public class ProductsSynchronizationPopUpCalendar extends DialogFragment {

    private CompositeDisposable compositeDisposable;
    private MealPlan mealPlan;
    private CheckBox checkBox;
    private TextView acceptButton;
    private TextView cancelButton;
    private View.OnClickListener listener;
    private SharedPreferences prefs;

    public ProductsSynchronizationPopUpCalendar() {
    }

    public ProductsSynchronizationPopUpCalendar(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
        listener = v -> {};
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewPopUpQuestionSynhronizationOnBinding binding = NewPopUpQuestionSynhronizationOnBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        compositeDisposable = new CompositeDisposable();

        setupViews(binding);
        setClickListeners();
        return dialog;
    }

    public void setupViews(NewPopUpQuestionSynhronizationOnBinding binding){
        checkBox = binding.checkBox;
        acceptButton = binding.buttonYes;
        cancelButton = binding.buttonNo;
    }
    public void setClickListeners(){
        cancelButton.setOnClickListener(v -> {
            Timber.e(mealPlan.toString());
            Toast.makeText(requireContext(), "Produkty nie zostały dodany do listy zakupów", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        acceptButton.setOnClickListener(v -> {
            if(checkBox.isChecked()){
                Toast.makeText(requireContext(), "Synchronizacja została włączona", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Synchronizacja nie została włączona", Toast.LENGTH_SHORT).show();
            }

            addIngredientsToShoppingList(v, checkBox.isChecked());
        });
    }

    public void addIngredientsToShoppingList(View v, boolean sync) {
        String token = "Bearer " + prefs.getString("token", "");
        ShoppingListRepository shoppingListRepository = new ShoppingListRepository();
        Disposable disposable = shoppingListRepository.synchronizeMealProducts(token, new MealShopPlan(mealPlan), sync)
                .subscribe(
                        response -> {
                            Toast.makeText(requireContext(), "Produkty zostały dodany do listy zakupów", Toast.LENGTH_SHORT).show();
                            listener.onClick(v);
                            dismiss();
                        },
                        error -> Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

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