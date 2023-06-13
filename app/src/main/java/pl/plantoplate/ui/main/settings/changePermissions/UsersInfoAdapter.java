package pl.plantoplate.ui.main.settings.changePermissions;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.models.UserInfo;

public class UsersInfoAdapter extends RecyclerView.Adapter<UsersInfoViewHolder>{

    private ArrayList<UserInfo> userInfos;
    private int itemType;

    private SetupUserPermissionsItems listener;

    public UsersInfoAdapter(ArrayList<UserInfo> products, int itemType) {
        this.userInfos = products;
        this.itemType = itemType;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUserInfosList(ArrayList<UserInfo> filterlist) {
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
        return new UsersInfoViewHolder(itemView, this.itemType);
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
