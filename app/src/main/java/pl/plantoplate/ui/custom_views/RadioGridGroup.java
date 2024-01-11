/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.plantoplate.ui.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.RadioButton;
import java.util.ArrayList;
import java.util.List;

/**
 * RadioGridGroup is a custom view that extends GridLayout and implements RadioGroup.OnCheckedChangeListener.
 * It is used in the application to display a grid of radio buttons.
 */
public class RadioGridGroup extends GridLayout{

    private final List<RadioButton> radioButtons = new ArrayList<>();
    private OnCheckedChangeListener onCheckedChangeListener;

    public RadioGridGroup(Context context) {
        super(context);
    }

    public RadioGridGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Adds a view to the grid layout.
     * @param child view to be added
     * @param index index of the view
     * @param params layout params
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setupRadioButton(child);
    }

    private void setupRadioButton(View child) {
        if (child instanceof RadioButton) {
            RadioButton radioButton = (RadioButton) child;
            radioButtons.add(radioButton);
            radioButton.setOnCheckedChangeListener(this::onRadioButtonChecked);
        }
    }

    private void onRadioButtonChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked && buttonView instanceof RadioButton) {
            RadioButton radioButton = (RadioButton) buttonView;
            uncheckOtherRadioButtons(radioButton);
            notifyOnCheckedChangeListener(radioButton.getId());
        }
    }

    private void uncheckOtherRadioButtons(RadioButton checkedButton) {
        for (RadioButton radioButton : radioButtons) {
            if (radioButton != checkedButton) {
                radioButton.setChecked(false);
            }
        }
    }

    private void notifyOnCheckedChangeListener(int checkedId) {
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(RadioGridGroup.this, checkedId);
        }
    }

    /**
     * Interface definition for a callback to be invoked when the checked radio button changed in this group.
     */
    public interface OnCheckedChangeListener {
        void onCheckedChanged(RadioGridGroup group, int checkedId);
    }

    /**
     * Register a callback to be invoked when the checked radio button changed in this group.
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    /**
     * Returns the checked radio button ID.
     * @return the checked radio button ID
     */
    public RadioButton getCheckedRadioButton() {
        for (RadioButton radioButton : radioButtons) {
            if (radioButton.isChecked()) {
                return radioButton;
            }
        }
        return null;
    }

    /**
     Set a specific radio button as checked by its ID.
     @param checkedId The ID of the radio button to set as checked.
     */
    public void setCheckedRadioButtonById(int checkedId) {
        for (RadioButton radioButton : radioButtons) {
            radioButton.setChecked(radioButton.getId() == checkedId);
        }
    }
}
