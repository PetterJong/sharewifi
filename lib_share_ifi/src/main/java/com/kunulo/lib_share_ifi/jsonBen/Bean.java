package com.kunulo.lib_share_ifi.jsonBen;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Bean {

    private String lang;
    private String code;
    private String time;
    private String source;
    private String sign;
    private String message;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "lang='" + lang + '\'' +
                ", code='" + code + '\'' +
                ", time='" + time + '\'' +
                ", source='" + source + '\'' +
                ", sign='" + sign + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
