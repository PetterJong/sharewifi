package com.kunulo.lib_share_ifi.net;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Error {
    public static final String REQUEST_SUCCES_CODE = "200";
    public static final String REQUEST_NO_RESULT_USER_ID= "201"; // 没有用户，需要重新登入下
    public static final String REQUEST_EXCEPTION_CODE = "-1";
    public static  String VERFI_CODE_ERROR  = "201"; // 验证码错误
    public static  int IS_NOT_EXITS_SESSION = 1001;// 不存在session；
    public static  String IS_NOT_EXITS_SESSION_MESSAGE = "找不到该用户信息";// 不存在session；
    public static  int  IS_HAVING_DEPOSIT = 1002;
    public static  String IS_HAVING_DEPOSIT_MESSAGE = "没有交押金";// 没有交押金
    public static  int  IS_DEVICE_NOT_BORROW = 1003; // 有设备没有归还
    public static  String IS_DEVICE_NOT_BORROW_MESSAGE = "有设备没有归还";// 没有交押金

    public static  int  IS_DEVICE_NOT_CLOSE = 1004; // 有设备没有结算
    public static  String IS_DEVICE_NOT_CLOSE_MESSAGE="有设备没结算";// 有设备没结算

    public static  int IS_NOT_BELONG_YOURDEVICE = 1005;// 不是你的设备
    public static  String IS_NOT_BELONG_YOURDEVICE_MESSAGE = "当前设备不是你所租借的设备";// 有设备没结算

}
