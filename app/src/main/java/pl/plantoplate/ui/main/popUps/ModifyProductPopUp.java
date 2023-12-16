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
package pl.plantoplate.ui.main.popUps;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.ui.customViews.RadioGridGroup;

public class ModifyProductPopUp extends Dialog {

    private TextView productName;
    private ImageView plusButton;
    private ImageView minusButton;
    private TextView closeButton;
    public TextInputEditText quantity;
    private TextView productUnitTextView;
    public TextView acceptButton;
    private RadioGroup radioGroup;
    private Button radioButton_min;
    private Button radioButton_middle;
    private Button radioButton_max;
    private HashMap<String,List<Float>> map = new HashMap<>();

    @SuppressLint("SetTextI18n")
    public ModifyProductPopUp(@NonNull Context context, Product product) {
        super(context);
        setCancelable(true);
        setContentView(R.layout.new_pop_up_change_in_product_quantity);

        productName = findViewById(R.id.text_head);
        plusButton = findViewById(R.id.plus);
        minusButton = findViewById(R.id.minus);
        closeButton = findViewById(R.id.close);
        quantity = findViewById(R.id.ilosc);
        productUnitTextView = findViewById(R.id.unit);
        acceptButton = findViewById(R.id.zatwierdzenie);
        radioGroup = findViewById(R.id.toggle_group);
        radioButton_min = findViewById(R.id.min);
        radioButton_middle = findViewById(R.id.middle);
        radioButton_max = findViewById(R.id.max);

        map.put("szt", List.of(0.5f,2.0f,5.0f));
        map.put("l", List.of(0.25f,0.5f,5.0f));
        map.put("ml", List.of(10.0f,50.0f,200.0f));
        map.put("kg", List.of(0.25f,0.5f,5.0f));
        map.put("gr", List.of(10.0f,50.0f,200.0f));

        String title = productName.getText().toString() + " " + product.getName();
        productName.setText(title);

        String unitTitle = productUnitTextView.getText().toString() + " " + product.getUnit().toLowerCase();
        productUnitTextView.setText(unitTitle);

        System.out.println(product.getUnit().toLowerCase());
        radioButton_min.setText("+" + String.valueOf(Objects.requireNonNull(map.get(product.getUnit().toLowerCase())).get(0)));
        radioButton_middle.setText("+" + String.valueOf(Objects.requireNonNull(map.get(product.getUnit().toLowerCase())).get(1)));
        radioButton_max.setText("+" + String.valueOf(Objects.requireNonNull(map.get(product.getUnit().toLowerCase())).get(2)));

        if(product.getAmount() == 0.0){
            quantity.setText("");
        }
        else{
            quantity.setText(String.valueOf(product.getAmount()));
        }

        quantity.requestFocus();
        quantity.setTag(product.getAmount());

        // set up input type
        setOnlyFloatInput();

        // set views listeners
        closeButton.setOnClickListener(v -> dismiss());
        plusButton.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(this.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                quantityValue = "0.0";
            }
            if (quantityValue.endsWith(".")) {
                // add 0
                quantityValue += "0";
                this.quantity.setText(quantityValue);
            }
            float quantity = Float.parseFloat(quantityValue);
            quantity++;
            this.quantity.setText(String.valueOf(quantity));
        });
        minusButton.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(this.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                quantityValue = "0.0";
            }
            if (quantityValue.endsWith(".")) {
                // add 0
                quantityValue += "0";
                this.quantity.setText(quantityValue);
            }
            float quantity = Float.parseFloat(quantityValue);
            if (quantity > 1) {
                quantity--;
                this.quantity.setText(String.valueOf(quantity));
            }
        });
        radioButton_min.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(this.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                quantityValue = "0.0";
            }
            if (quantityValue.endsWith(".")) {
                // add 0
                quantityValue += "0";
                this.quantity.setText(quantityValue);
            }
            float quantity = Float.parseFloat(quantityValue);
            quantity += Objects.requireNonNull(map.get(product.getUnit().toLowerCase())).get(0);
            this.quantity.setText(String.valueOf(quantity));
        });
        radioButton_middle.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(this.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                quantityValue = "0.0";
            }
            if (quantityValue.endsWith(".")) {
                // add 0
                quantityValue += "0";
                this.quantity.setText(quantityValue);
            }
            float quantity = Float.parseFloat(quantityValue);
            quantity += Objects.requireNonNull(map.get(product.getUnit().toLowerCase())).get(1);
            this.quantity.setText(String.valueOf(quantity));
        });
        radioButton_max.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(this.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                quantityValue = "0.0";
            }
            if (quantityValue.endsWith(".")) {
                // add 0
                quantityValue += "0";
                this.quantity.setText(quantityValue);
            }
            float quantity = Float.parseFloat(quantityValue);
            quantity += Objects.requireNonNull(map.get(product.getUnit().toLowerCase())).get(2);
            this.quantity.setText(String.valueOf(quantity));
        });

        this.setOnShowListener(dialog -> {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        });

        setOnDismissListener(dialog -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        });


    }

    public void setOnlyFloatInput() {
        quantity.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                try {
                    float inputFloat = Float.parseFloat(input);
                    if (inputFloat > 9999.99) {
                        input = "9999.99";
                    }
                } catch (NumberFormatException e) {
                    if (input.equals(".") || input.isEmpty()) {
                        input = "1.0";
                    } else {
                        input = quantity.getTag() != null ? quantity.getTag().toString() : "";
                    }
                }
                if (!input.equals(Objects.requireNonNull(quantity.getText()).toString())) {
                    quantity.setText(input);
                    quantity.setSelection(input.length());
                }

                quantity.setTag(input);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }
}
