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

package pl.plantoplate.ui.customViewes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RadioGridGroup is a custom view that extends GridLayout and implements RadioGroup.OnCheckedChangeListener.
 * It is used in the application to display a grid of radio buttons.
 */
public class RadioGridGroup extends GridLayout implements RadioGroup.OnCheckedChangeListener {

    private List<RadioButton> radioButtons = new ArrayList<>();
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
        if (child instanceof RadioButton) {
            radioButtons.add((RadioButton) child);
            ((RadioButton) child).setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    for (RadioButton radioButton : radioButtons) {
                        if (radioButton != buttonView) {
                            radioButton.setChecked(false);
                        }
                    }
                    if (onCheckedChangeListener != null) {
                        onCheckedChangeListener.onCheckedChanged(RadioGridGroup.this, buttonView.getId());
                    }
                }
            });
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // do nothing
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(RadioGridGroup group, int checkedId);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public RadioButton getCheckedRadioButton() {
        for (RadioButton radioButton : radioButtons) {
            if (radioButton.isChecked()) {
                return radioButton;
            }
        }
        return null;
    }
}
