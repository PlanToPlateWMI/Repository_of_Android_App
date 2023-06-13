package pl.plantoplate.ui.main.settings.changePermissions;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.repository.remote.models.UserInfo;

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
