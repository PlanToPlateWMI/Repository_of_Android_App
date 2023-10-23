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

package pl.plantoplate.ui.main.settings.changePermissions;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.UserInfo;

public class UsersInfoViewHolder extends RecyclerView.ViewHolder {

    private TextView userName;
    private TextView userPermissions;
    private ImageView editUser;

    private int itemType;

    /**
     * Constructor for the UsersInfoViewHolder class.
     *
     * @param itemView The root view of the item layout.
     * @param itemType The type of the item view.
     */
    public UsersInfoViewHolder(View itemView, int itemType) {
        super(itemView);
        this.itemType = itemType;
        userName = itemView.findViewById(R.id.user_name);
        userPermissions = itemView.findViewById(R.id.user_permissions);
        editUser = itemView.findViewById(R.id.iconEdit_user);

    }

    /**
     * Binds the user information to the view holder.
     *
     * @param userInfo The user information to bind.
     * @param listener The listener for setup user permissions items.
     */
    public void bind(UserInfo userInfo, SetupUserPermissionsItems listener) {

        userName.setText(userInfo.getUsername());
        String role = "";
        if (userInfo.getRole().equals("ROLE_ADMIN")){
            role = "Administrator";
        } else if (userInfo.getRole().equals("ROLE_USER")){
            role = "Użytkownik";
        } else {
            role = "Nieznana rola";
        }
        userPermissions.setText(role);

        listener.setupEditPermissionsButtonClick(editUser, userInfo);
    }
}
