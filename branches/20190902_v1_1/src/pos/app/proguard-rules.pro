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

-optimizationpasses 5 # 指定代码的压缩级别
-dontusemixedcaseclassnames # 纯小写混合类名
-dontskipnonpubliclibraryclasses # 不忽略非公共库的类
-dontskipnonpubliclibraryclassmembers # 不忽略非公共库的类
-dontpreverify # 不做预校验
-verbose # 混淆时记录日志
-allowaccessmodification # 优化时允许访问并修改有修饰符的类和类的成员
-mergeinterfacesaggressively # 混淆时应用侵入式重载

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/* # 混淆时所采用的算法

-keepattributes *Annotation* # 保护代码中的Annotation不被混淆
-keepattributes Signature # 避免混淆泛型
-renamesourcefileattribute ProGuard # 重命名被混淆文件名
-keepattributes SourceFile,LineNumberTable # 抛出异常时保留代码行号
-ignorewarnings

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface

-keep public class * extends android.app.Activity # 保持哪些类不被混淆
-keep public class * extends android.app.Application # 保持哪些类不被混淆
-keep public class * extends android.app.Service # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference # 保持哪些类不被混淆
-keep public class * extends android.view.View # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService # 保持哪些类不被混淆
-optimizations !class/unboxing/enum

-keep public class com.tungee.dongke.R$*{
public static final int *;
}

# 保留在Activity中的方法参数是view的方法，从而我们在layout里面编写onClick就不会被影响
-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
native <methods>;
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 枚举类不能被混淆
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

# 保留自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View {
*** get*();
void set*(***);
public <init>(android.content.Context);
public <init>(android.content.Context, android.util.AttributeSet);
public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化的类不被混淆
-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
static final long serialVersionUID;
private static final java.io.ObjectStreamField[] serialPersistentFields;
private void writeObject(java.io.ObjectOutputStream);
private void readObject(java.io.ObjectInputStream);
java.lang.Object writeReplace();
java.lang.Object readResolve();
}

# 对于R（资源）下的所有类及其方法，都不能被混淆
-keep class **.R$* {
*;
}

# 对于带有回调函数onXXEvent的，不能被混淆
-keepclassmembers class * {
void *(**on*Event);
}

# 对WebView的处理
-keepclassmembers class * extends android.webkit.webViewClient {
public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
public void *(android.webkit.webView, java.lang.String);
}

# support-v7-appcompat
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}
# support-design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# butterknife begin
# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(...); }
# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
# butterknife end

# glide begin
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
**[] $VALUES;
public *;
}
# glide end

# retrofit & okhttp
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
## Retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-keepclassmembernames interface * {
@retrofit2.http.* <methods>;
}

## okhttp
# 尝试解决:Exception java.lang.IncompatibleClassChangeError: interface not implemented
-keep class okhttp3.** {*;}

#去掉android.log
-assumenosideeffects class android.util.Log {
public static boolean isLoggable(java.lang.String, int);
public static *** d(...);
public static *** v(...);
public static *** i(...);
public static *** w(...);
public static *** e(...);
}

# 去掉trace
-assumenosideeffects class android.os.Debug {
public static void startMethodTracing();
public static void stopMethodTracing();
public static void startNativeTracing();
public static void startNativeTracing();
public static void startAllocCounting();
public static void stopAllocCounting();
}

# Keep @Keep 注解标注的对象,这种对象都是json对象,混淆了就不能解析json了
-keepclasseswithmembers @android.support.annotation.Keep public class ** {
*;
}

# eventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# green dao
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use Rx:
-dontwarn rx.**

##---------------Begin: proguard configuration for Gson ----------
-keep public class com.google.gson.**
-keep public class com.google.gson.** {public private protected *;}
-keepattributes Signature
-keepattributes *Annotation*
-keep public class com.project.mocha_patient.login.SignResponseData { private *; }
##---------------End: proguard configuration for Gson ----------

## 不要让混淆器混淆所有我们自己写的MODEL
-keep public class com.bx.erp.model.**
-keep public class com.bx.erp.model.** {public private protected *;}
-keepattributes Signature
-keepattributes *Annotation*
##