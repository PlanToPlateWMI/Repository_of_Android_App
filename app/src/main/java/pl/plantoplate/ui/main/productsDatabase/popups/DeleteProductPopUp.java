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

package pl.plantoplate.ui.main.productsDatabase.popups;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import pl.plantoplate.R;

public class DeleteProductPopUp extends Dialog {

    public DeleteProductPopUp(Context context, View.OnClickListener listener) {
        super(context);
        setCancelable(true);
        setContentView(R.layout.new_pop_up_delete_product_from_shopping_list);
        //getWindow().setWindowAnimations(R.style.DialogAnimation);

        TextView acceptButton = findViewById(R.id.button_yes);
        TextView cancelButton = findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {
            listener.onClick(v);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }
}
