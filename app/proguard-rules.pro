# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Dexter\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-repackageclasses 'obfuscated'
-keepattributes *Annotation*
# Add this global rule
-keepattributes Signature
-keepattributes EnclosingMethod

-dontwarn okio.**
-dontwarn retrofit2.**
-dontwarn com.squareup.picasso.**
-keepclassmembers enum com.rohitsuratekar.NCBSinfo.** { *; }
-keep class com.rohitsuratekar.NCBSinfo.background.networking.** {*;}
-keep class com.rohitsuratekar.NCBSinfo.database.models.** {*;}