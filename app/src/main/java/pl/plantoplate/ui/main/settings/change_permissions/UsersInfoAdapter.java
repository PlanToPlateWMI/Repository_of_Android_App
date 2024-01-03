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

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import pl.plantoplate.data.remote.models.user.UserInfo;

public class UsersInfoAdapter extends RecyclerView.Adapter<UsersInfoViewHolder>{

    private final int itemType;
    private List<UserInfo> userInfos;
    private SetupUserPermissionsItems listener;

    public UsersInfoAdapter(List<UserInfo> products, int itemType) {
        this.userInfos = products;
        this.itemType = itemType;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUserInfosList(List<UserInfo> filterlist) {
        userInfos = filterlist;
        notifyDataSetChanged();
    }

    public void setUpItemButtons(SetupUserPermissionsItems listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsersInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(this.itemType, parent, false);
        return new UsersInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersInfoViewHolder holder, int position) {
        holder.bind(userInfos.get(position), listener);
    }

    @Override
    public int getItemCount() {
        if(userInfos == null)
            return 0;
        return userInfos.size();
    }
}