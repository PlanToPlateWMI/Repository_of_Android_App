package pl.plantoplate.tools;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

public class ServiceHelper {

    public static void disableService(Context context, Class<?> serviceClass) {
        ComponentName componentName = new ComponentName(context, serviceClass);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        );
    }

    public static void enableService(Context context, Class<?> serviceClass) {
        ComponentName componentName = new ComponentName(context, serviceClass);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        );
    }
}