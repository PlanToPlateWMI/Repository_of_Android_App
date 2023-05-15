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

public class RadioGridGroup extends GridLayout implements RadioGroup.OnCheckedChangeListener {

    private List<RadioButton> radioButtons = new ArrayList<>();
    private OnCheckedChangeListener onCheckedChangeListener;

    public RadioGridGroup(Context context) {
        super(context);
    }

    public RadioGridGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

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
}
