package com.kunulo.lib_share_ifi.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SdkUtils {
    private static SharedPreferences shrefs;

    public static void saveUserToken(Context context, String userToken){
        if(shrefs == null){
            shrefs = context.getSharedPreferences("share_ifi_spf", context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = shrefs.edit();
        editor.putString("user_token", userToken);
        editor.commit();
    }

    public static String getUserToken(Context context){
        if(shrefs == null){
            shrefs = context.getSharedPreferences("share_ifi_spf", context.MODE_PRIVATE);
        }
        return shrefs.getString("user_token","");
    }

    public static void saveUserNumber(Context context, String userNumber){
        if(shrefs == null){
            shrefs = context.getSharedPreferences("share_ifi_spf", context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = shrefs.edit();
        editor.putString("user_number", userNumber);
        editor.commit();
    }

    public static String getUserNumber(Context context){
        if(shrefs == null){
            shrefs = context.getSharedPreferences("share_ifi_spf", context.MODE_PRIVATE);
        }
        return shrefs.getString("user_number","");
    }

    public static void clearData(Context context){
        saveUserNumber(context, "");
        saveUserToken(context,"");
    }

}
