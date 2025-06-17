# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# =======================
# MediaPipe + TensorFlow Lite
# =======================

# Jaga class MediaPipe Tasks tetap utuh
-keep class com.google.mediapipe.** { *; }
-keep class org.tensorflow.** { *; }

# Hindari masalah refleksi
-keepclassmembers class * {
    @com.google.mediapipe.tasks.annotation.UsedByNative <methods>;
    @com.google.mediapipe.tasks.annotation.UsedByReflection <fields>;
    @com.google.mediapipe.tasks.annotation.UsedByReflection <methods>;
}


# Hindari menghapus anotasi penting
-keepattributes *Annotation*

# Jangan obfuscate class-model dari TFLite
-keep class org.tensorflow.lite.task.** { *; }

# Untuk CameraX (jika shrink mode aktif)
-keep class androidx.camera.** { *; }

# Optional: untuk debugging
-keepattributes SourceFile,LineNumberTable
