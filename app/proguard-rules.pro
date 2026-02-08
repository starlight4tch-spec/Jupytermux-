-keep class com.google.gson.** { *; }
-keepclassmembers class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keepclassmembers class * implements com.google.gson.JsonDeserializer {
    <init>(...);
}
-keepclassmembers class * implements com.google.gson.JsonSerializer {
    <init>(...);
}

-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**
-dontwarn okhttp3.**
-dontwarn okio.**

-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**

-keepattributes InnerClasses
-keepattributes Signature
-keepattributes *Annotation*
