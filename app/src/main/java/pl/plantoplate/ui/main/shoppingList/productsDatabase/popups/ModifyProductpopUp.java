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

package pl.plantoplate.ui.main.shoppingList.productsDatabase.popups;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.repository.remote.models.Product;

public class ModifyProductpopUp extends Dialog {

    private TextView productName;
    private ImageView plusButton;
    private ImageView minusButton;
    private TextView closeButton;
    public TextInputEditText quantity;
    private TextView productUnitTextView;
    public TextView acceptButton;

    public ModifyProductpopUp(@NonNull Context context, Product product) {
        super(context);
        // set up dialog parameters
        setCancelable(true);
        setContentView(R.layout.new_pop_up_change_in_product_quantity);

        // find views
        productName = findViewById(R.id.text_head);
        plusButton = findViewById(R.id.plus);
        minusButton = findViewById(R.id.minus);
        closeButton = findViewById(R.id.close);
        quantity = findViewById(R.id.ilosc);
        productUnitTextView = findViewById(R.id.unit);
        acceptButton = findViewById(R.id.zatwierdzenie);

        // set up views
        String title = productName.getText().toString() + " " + product.getName();
        productName.setText(title);

        String unitTitle = productUnitTextView.getText().toString() + " " + product.getUnit();
        productUnitTextView.setText(unitTitle);

        quantity.setText(String.valueOf(product.getAmount()));

        quantity.requestFocus();
        quantity.setTag(product.getAmount());

        // set up input type
        setOnlyFloatInput();

        // set views listeners
        closeButton.setOnClickListener(v -> dismiss());
        plusButton.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(this.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                quantityValue = "1.0";
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
                quantityValue = "1.0";
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

        this.setOnShowListener(dialog -> {

            // show numeric keyboard
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        });

        setOnDismissListener(dialog -> {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        });


    }

    public void setOnlyFloatInput() {
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String previousInput = quantity.getTag() != null ? quantity.getTag().toString() : "";

                // check if input doesn't start with a dot
                if (input.startsWith(".")) {
                    input = previousInput;
                }

                // check if input doesn't have more than one dot
                if (countDots(input) > 1) {
                    input = previousInput;
                }

                // check if input float <= 9999.99
                // parse input to float
                float inputFloat;
                if (!input.isEmpty()) {
                    inputFloat = Float.parseFloat(input);
                    if (inputFloat > 9999.99) {
                        input = previousInput;
                    }
                }

                // set input
                if (!input.equals(Objects.requireNonNull(quantity.getText()).toString())) {
                    quantity.setText(input);
                    quantity.setSelection(input.length());
                }

                quantity.setTag(input);
            }
        });
    }

    public int countDots(String input) {
        int dotCount = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '.') {
                dotCount++;
            }
        }
        return dotCount;
    }
}
