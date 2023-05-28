package pl.plantoplate.ui.main.shoppingList.productsDatabase.popups;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.repository.models.Product;

public class AddToCartPopUp extends Dialog {

    private TextView productName;
    private ImageView plusButton;
    private ImageView minusButton;
    private ImageView closeButton;
    public TextInputEditText quantity;
    private TextView productUnitTextView;
    public Button acceptButton;


    public AddToCartPopUp(@NonNull Context context, Product product) {
        super(context);
        // set up dialog parameters
        setCancelable(true);
        setContentView(R.layout.pop_up_change_in_product_quantity);

        // find views
        productName = findViewById(R.id.add_products_text);
        plusButton = findViewById(R.id.plus);
        minusButton = findViewById(R.id.minus);
        closeButton = findViewById(R.id.close);
        quantity = findViewById(R.id.ilosc);
        productUnitTextView = findViewById(R.id.jednostki_miary_napisac);
        acceptButton = findViewById(R.id.zatwierdzenie);

        // set up views
        productName.setText(product.getName());
        quantity.setText(String.valueOf(product.getAmount()));
        productUnitTextView.setText(product.getUnit());

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
                // Remove the dot
                quantityValue = quantityValue.substring(0, quantityValue.length() - 1);
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
                // Remove the dot
                quantityValue = quantityValue.substring(0, quantityValue.length() - 1);
                this.quantity.setText(quantityValue);
            }
            float quantity = Float.parseFloat(quantityValue);
            if (quantity > 1) {
                quantity--;
                this.quantity.setText(String.valueOf(quantity));
            }
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
                if (!input.isEmpty() && Float.parseFloat(input) > 9999) {
                    quantity.setText("9999");
                }
                if (input.startsWith(".") || input.replaceAll("[^.]", "").length() > 1) {
                    // Replace with previous value
                    if (quantity.getTag() == null) {
                        // If there is no previous value, replace with empty string
                        quantity.setText("");
                    } else {
                        // If there is a previous value, replace with it
                        String previousValue = quantity.getTag().toString();
                        quantity.setText(previousValue);
                        // Move the cursor to the end of the text
                        quantity.setSelection(previousValue.length());
                    }
                } else {
                    // Save the current value as the previous value
                    quantity.setTag(input);
                }
            }
        });
    }
}
