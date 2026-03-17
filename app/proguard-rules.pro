# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Keep Room entities
-keep class com.smartpantry.data.model.** { *; }
