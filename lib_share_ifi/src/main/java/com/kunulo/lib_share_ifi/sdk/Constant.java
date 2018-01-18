package com.kunulo.lib_share_ifi.sdk;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/08/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Constant {
//    public static final String BASE_RUL = "http://192.168.0.104:8011/remote/v1/";
    public static final String BASE_RUL = "https://ifi.kunulo.cn/remote/v1/";
    public static final String URL_GET_SMS_CODE = BASE_RUL + "smscode/"; // 获取验证码（post）
    public static final String URL_LOGIN = BASE_RUL + "sessions/";  // 登入（post）
    public static final String URL_IS_PAY_DEPOSIT = BASE_RUL + "wallet/status/";// 是否交押金（post）
    public static final String URL_IS_CAN_BORROW = BASE_RUL + "mydevices/status/"; // 是否能租借设备（post）
    public static final String URL_NEARBY_DEVICE = BASE_RUL + "areas/nearby/"; // 获取附近设备（post）
    public static final String URL_GET_PASSWORD = BASE_RUL + "keys/";  // 获取设备密码（post）
    public static final String URL_RENT_DEVICE = BASE_RUL + "devices/"; // 租设备（post）
//    public static final String URL_RETURN_DEVICE = BASE_RUL + "devices/";// 还设备 （put）
    public static final String URL_RETURN_DEVICE = "https://ifi.kunulo.cn:8011/ifi/web/back";// 还设备 （put）
    public static final String URL_GET_MY_DEVICE = BASE_RUL + "mydevices/device/"; // 获取我借用的设备（post）
    public static final String URL_GET_PAY_NUM = BASE_RUL + "wallet/paynum/"; // 获取需支付金额（post）
    public static final String URL_GET_MY_ORDER= BASE_RUL + "myOrders/"; // 获取需支付金额（post）
    public static final String URL_PAY = BASE_RUL + "pay/"; // 支付金额（post）
    public static final String URL_RESET_PASSWORD = BASE_RUL + "reset/"; // 支付金额（post）

    public static final int WHAT_GET_SMS_CODE = 0x1001;
    public static final int WHAT_LOGIN = 0x1002;
    public static final int WHAT_IS_PAY_DEPOSIT = 0x1003;
    public static final int WHAT_IS_CAN_BORROW  = 0x1004;
    public static final int WHAT_NEARBY_DEVICE  = 0x1005;
    public static final int WHAT_GET_PASSWORD = 0x1006;
    public static final int WHAT_RENT_DEVICE = 0x1007;
    public static final int WHAT_RETURN_DEVICE = 0x1008;
    public static final int WHAT_GET_MY_DEVICE = 0x1009;
    public static final int WHAT_GET_PAY_NUM = 0x100A;
    public static final int WHAT_GET_MY_ORDER = 0x100B;
    public static final int WHAT_PAY= 0x100C;
    public static final int WHAT_RESET_PASSWORD= 0x100D;

    public static int nearDistance = 1 ;
    public static String deviceToken = "" ;
    public static String userToken = "" ;
    public static String userNumber = "" ;
    public static String userId = "" ;
    public static String imei = "";

    public static final String WX_APP_ID = "wxa7793758f9a7fa4e";
    public static final String WX_APP_SECRET = "a3799852befdff92261b04e268639cf0";
}
