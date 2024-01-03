# Disable android logging
-assumenosideeffects class android.util.Log {*;}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keepattributes Signature
-keepattributes Annotation

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keepattributes Signature
-keepattributes Annotation

-keep class pl.plantoplate.** { *; }

# RxJava
-dontwarn sun.misc.**
-dontwarn javax.xml.**
-dontwarn org.codehaus.**
-dontwarn rx.internal.util.**
-dontwarn com.google.**
-dontwarn io.reactivex.**
-dontwarn retrofit2.Platform$Java8.*

# If using RxJava3
-keep class io.reactivex.rxjava3.** { *; }
-keep interface io.reactivex.rxjava3.** { *; }
-keep class retrofit2.adapter.rxjava3.** { *; }

# Firebase Cloud Messaging
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers class com.google.firebase.messaging.** {
    *;
}

-keepclassmembers class * extends com.google.firebase.messaging.FirebaseMessagingService {
    *;
}