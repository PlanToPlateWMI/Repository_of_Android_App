package pl.plantoplate.ui.main.settings.changePermissions;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.repository.remote.models.UserInfo;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;

public class UsersInfoViewHolder extends RecyclerView.ViewHolder {

    private TextView userName;
    private TextView userPermissions;
    private ImageView editUser;

    private int itemType;

    public UsersInfoViewHolder(View itemView, int itemType) {
        super(itemView);
        this.itemType = itemType;
        userName = itemView.findViewById(R.id.user_name);
        userPermissions = itemView.findViewById(R.id.user_permissions);
        editUser = itemView.findViewById(R.id.iconEdit_user);

    }

    public void bind(UserInfo userInfo, SetupUserPermissionsItems listener) {

        userName.setText(userInfo.getUsername());
        userPermissions.setText(userInfo.getRole());

        editUser.setOnClickListener(listener::setupEditPermissionsButtonClick);
    }
}
