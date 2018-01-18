package com.kunulo.share_ifi.utils;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PaymentRequest {

    private static String YOUR_URL ="http://218.244.151.190/demo/charge";
    public static final String CHARGE_URL = YOUR_URL;
    public static final boolean LIVEMODE = true;
    /**
     * 银联支付渠道
     */
    public static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    public static final String CHANNEL_WECHAT = "wx";
    /**
     * QQ钱包支付渠道
     */
    public static final String CHANNEL_QPAY = "qpay";
    /**
     * 支付宝支付渠道
     */
    public static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    public static final String CHANNEL_BFB = "bfb_wap";
    /**
     * 京东支付渠道
     */
    public static final String CHANNEL_JDPAY_WAP = "jdpay_wap";


    private String channel;
    private int amount;
    private boolean livemode;

    public PaymentRequest(String channel, int amount) {
        this.channel = channel;
        this.amount = amount;
        this.livemode = LIVEMODE;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }
}
