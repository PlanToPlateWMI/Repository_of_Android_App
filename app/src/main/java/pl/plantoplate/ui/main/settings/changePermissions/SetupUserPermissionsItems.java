package pl.plantoplate.ui.main.settings.changePermissions;

import android.view.View;

import pl.plantoplate.repository.remote.models.UserInfo;

public interface SetupUserPermissionsItems {

    void setupEditPermissionsButtonClick(View v, UserInfo userInfo);
}
