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
public class LoginJsonBean extends Bean {
    //    {
//        "lang": null,
//            "code": null,
//            "time": null,
//            "source": null,
//            "value": {
//        "user_info": {
//            "userId": "1"
//        },
//        "user_token": "455488888882fff88ff4s4sdsd5sd5ds5"
//    },
//        "sign": null
//    }
    private LoginValue value;

    public LoginValue getValue() {
        return value;
    }

    public void setValue(LoginValue value) {
        this.value = value;
    }

    public class LoginValue {
        private String userToken;
        private String userId;

        public String getUserToken() {
            return userToken;
        }

        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "LoginValue{" +
                    "userToken='" + userToken + '\'' +
                    ", userId='" + userId + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginJsonBean{" +
                "value=" + value +
                '}';
    }

    public String getUserId(){
        return value.userId;
    }

    public String getUserToken(){
        return value.userToken;
    }

}
