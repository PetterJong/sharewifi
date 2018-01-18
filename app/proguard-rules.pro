# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk/tools/proguard/proguard-android.txt
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

#-----------------混淆配置设定------------------------------------------------------------------------
-optimizationpasses 5                                                       #指定代码压缩级别
-dontusemixedcaseclassnames                                                 #混淆时不会产生形形色色的类名
-dontskipnonpubliclibraryclasses                                            #指定不忽略非公共类库
-dontpreverify                                                              #不预校验，如果需要预校验，是-dontoptimize
-ignorewarnings                                                             #屏蔽警告
-verbose                                                                    #混淆时记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    #优化

#-----------------不需要混淆系统组件等-------------------------------------------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#-----------------不需要混淆第三方类库------------------------------------------------------------------
-dontwarn android.support.v4.**                                             #去掉警告
-keep class android.support.v4.** { *; }                                    #过滤android.support.v4
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

#----------------保护指定的类和类的成员，但条件是所有指定的类和类成员是要存在------------------------------------
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


# zxing扫码混淆配置
 -keep class com.google.zxing.** {*;}
 -dontwarn com.google.zxing.**
 -keep class com.uuzuche.lib_zxing.** {*;}
 -dontwarn com.uuzuche.lib_zxing.**

# 百度地图开发混淆配置
 -keep class com.baidu.** {*;}
 -keep class vi.com.** {*;}
 -dontwarn com.baidu.**

# utilcode混淆配置
 -keep class com.blankj.utilcode.** { *; }
 -keepclassmembers class com.blankj.utilcode.** { *; }
 -dontwarn com.blankj.utilcode.**

# umeng 混淆配置
 -dontwarn com.taobao.**
 -dontwarn anet.channel.**
 -dontwarn anetwork.channel.**
 -dontwarn org.android.**
 -dontwarn org.apache.thrift.**
 -dontwarn com.xiaomi.**
 -dontwarn com.huawei.**
 -keepattributes *Annotation*
 -keep class com.taobao.** {*;}
 -keep class org.android.** {*;}
 -keep class anet.channel.** {*;}
 -keep class com.umeng.** {*;}
 -keep class com.xiaomi.** {*;}
 -keep class com.huawei.** {*;}
 -keep class org.apache.thrift.** {*;}
 -keep class com.alibaba.sdk.android.**{*;}
 -keep class com.ut.**{*;}
 -keep class com.ta.**{*;}
 -keep public class **.R$*{
    public static final int *;
 }
# umeng统计代码混淆
 -keepclassmembers class * {
    public <init> (org.json.JSONObject);
 }
 -keep public class com.kunulo.share_ifi.R$*{
 public static final int *;
 }
 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 #（可选）避免Log打印输出
 -assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
  }

  # okhtt混淆配置
  -dontwarn okio.**
  -dontwarn javax.annotation.Nullable
  -dontwarn javax.annotation.ParametersAreNonnullByDefault

  # Ping++ 混淆过滤
  -dontwarn com.pingplusplus.**
  -keep class com.pingplusplus.** {*;}

  # 支付宝混淆过滤
  -dontwarn com.alipay.**
  -keep class com.alipay.** {*;}

  # 微信或QQ钱包混淆过滤
  -dontwarn  com.tencent.**
  -keep class com.tencent.** {*;}

  # 银联支付混淆过滤
  -dontwarn  com.unionpay.**
  -keep class com.unionpay.** {*;}

  # 招行一网通混淆过滤
  -keepclasseswithmembers class cmb.pb.util.CMBKeyboardFunc {
      public <init>(android.app.Activity);
      public boolean HandleUrlCall(android.webkit.WebView,java.lang.String);
      public void callKeyBoardActivity();
  }

  # 内部WebView混淆过滤
  -keepclassmembers class * {
      @android.webkit.JavascriptInterface <methods>;
  }