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
package pl.plantoplate.ui.main.settings.change_permissions;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Map;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.user.UserInfo;

public class UsersInfoViewHolder extends RecyclerView.ViewHolder {

    private final TextView userNameTextView;
    private final TextView userPermissionsButton;
    private final ImageView editUserButton;

    /**
     * Constructor for the UsersInfoViewHolder class.
     *
     * @param itemView The root view of the item layout.
     */
    public UsersInfoViewHolder(View itemView) {
        super(itemView);
        userNameTextView = itemView.findViewById(R.id.user_name);
        userPermissionsButton = itemView.findViewById(R.id.user_permissions);
        editUserButton = itemView.findViewById(R.id.iconEdit_user);
    }

    /**
     * Binds the user information to the view holder.
     *
     * @param userInfo The user information to bind.
     * @param listener The listener for setup user permissions items.
     */
    public void bind(UserInfo userInfo, SetupUserPermissionsItems listener) {
        userNameTextView.setText(userInfo.getUsername());
        Map<String, String> roleMappings = Map.of(
                "ROLE_ADMIN", "Administrator",
                "ROLE_USER", "Użytkownik"
        );

        String role = roleMappings.getOrDefault(userInfo.getRole(), "Nieznana rola");
        userPermissionsButton.setText(role);
        listener.setupEditPermissionsButtonClick(editUserButton, userInfo);
    }
}