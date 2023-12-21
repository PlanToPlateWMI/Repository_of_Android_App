package pl.plantoplate.ui.main.settings.helpManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import pl.plantoplate.databinding.FragmentDeveloperBinding;
import pl.plantoplate.databinding.FragmentHelpBinding;

public class HelpManager extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHelpBinding fragmentHelpBinding = FragmentHelpBinding.inflate(inflater,
                container, false);

//        initViews(fragmentDeveloperBinding);
//        setClickListeners();
        return fragmentHelpBinding.getRoot();
    }

    /**
     * Initialize the views
     * @param fragmentDeveloperBinding The binding object
     */
    public void initViews(FragmentDeveloperBinding fragmentDeveloperBinding) {
        //acceptButton = fragmentDeveloperBinding.buttonZatwierdz;
    }

    /**
     * Set the click listeners
     */
    public void setClickListeners() {
        //acceptButton.setOnClickListener(v -> sendMail());
    }

}
