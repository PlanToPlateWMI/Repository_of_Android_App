package pl.plantoplate.service.push_notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Objects;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.ActivityMain;
import timber.log.Timber;

public class PushNotificationService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Timber.e("onNewToken: %s", token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Timber.e("onMessageReceived: %s", Objects.requireNonNull(message.getNotification()).getBody());
        getFirebaseMessage(Objects.requireNonNull(message.getNotification()).getTitle(), message.getNotification().getBody());
    }

    private void getFirebaseMessage(String title, String body) {
        Intent intent = new Intent(this, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(body);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notify")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("PlanTo Plate")
                .setContentText(title)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setStyle(bigTextStyle);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Timber.e("No permission to post notifications");
            return;
        }

        if (title.matches("Your role was changed to .*")) {
            updateRole();
        }

        createNotificationChannel();
        notificationManagerCompat.notify(generateUniqueNotificationId(), builder.build());
    }

    private void createNotificationChannel() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            NotificationChannel channel = new NotificationChannel("notify", "notify", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void updateRole() {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getString("role", "").equals("ROLE_USER")) {
            editor.putString("role", "ROLE_ADMIN");
        } else {
            editor.putString("role", "ROLE_USER");
        }
        editor.apply();
    }

    private int generateUniqueNotificationId() {
        // Генерируем уникальный ID для каждого уведомления, например, на основе временной метки или случайного числа.
        return (int) System.currentTimeMillis();
    }
}
