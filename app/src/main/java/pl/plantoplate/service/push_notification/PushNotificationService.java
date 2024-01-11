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
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.FCMToken;
import pl.plantoplate.data.remote.repository.FCMTokenRepository;
import pl.plantoplate.ui.main.ActivityMain;
import timber.log.Timber;

/**
 * Service for handling Firebase push notifications.
 */
public class PushNotificationService extends FirebaseMessagingService {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String DEFAULT_CHANNEL = "default";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FCMToken fcmToken = new FCMToken(token);
        FCMTokenRepository fcmTokenRepository = new FCMTokenRepository();
        Disposable disposable = fcmTokenRepository.updateFcmToken(token, fcmToken)
                .subscribe(msg -> Timber.e("onNewToken: %s", msg),
                        throwable -> Timber.e("onNewToken: %s", throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    /**
     * Called when message is received.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Timber.e("onMessageReceived: %s", Objects.requireNonNull(message.getNotification()).getBody());
        getFirebaseMessage(Objects.requireNonNull(message.getNotification()).getTitle(), message.getNotification().getBody());
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void getFirebaseMessage(String title, String body) {
        Intent intent = new Intent(this, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(body);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, DEFAULT_CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("PlanToPlate")
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

    /**
     * Create notification channel for Android Oreo and higher.
     */
    private void createNotificationChannel() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            NotificationChannel channel = new NotificationChannel(DEFAULT_CHANNEL, DEFAULT_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Update user role in SharedPreferences.
     */
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

    /**
     * Generate unique notification ID.
     */
    private int generateUniqueNotificationId() {
        // Generate unique notification ID
        return (int) System.currentTimeMillis();
    }

    /**
     * Clear all subscriptions.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}

