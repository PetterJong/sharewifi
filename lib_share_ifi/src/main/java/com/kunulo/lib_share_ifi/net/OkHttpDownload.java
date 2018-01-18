package com.kunulo.lib_share_ifi.net;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.utils.LogUtils;
import com.kunulo.lib_share_ifi.sdk.Constant;
import com.kunulo.lib_share_ifi.util.Base64;
import com.kunulo.lib_share_ifi.util.MD5Util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/08/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class OkHttpDownload {
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static SimpleDateFormat spdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static FormBody.Builder emptyBudlider = new FormBody.Builder();

    public enum Method{
        GET, POST, PUT
    }



    /**
     * get请求
     * @param url
     * @param params
     * @param callback
     */
    public static void httpRequestJsonForGet(final String url, Map<String, Object> params, final Callback callback) {
        String json = baseEncodeJson(getJsonParams(params));
        String sign = MD5Util.MD5Encode(json); //sign;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("params", json == null ? "" : json)
                .addHeader("time", spdf.format(new Date()))
                .addHeader("source", "20")
                .addHeader("sign", sign == null ? "" : sign)
                .addHeader("device_token", Constant.deviceToken)
                .addHeader("lang", "zh_CN")
                .addHeader("user_token", Constant.userToken)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               callback.onResponse(call, response);
            }
        });
    }

    /**
     * post请求
     * @param url
     * @param params
     * @param callback
     */
    public static void httpRequestJson(Method method, final String url, Map<String, Object> params, final Callback callback){
        String data = baseEncodeJson(getJsonParams(params));
//        String data = getJsonParams(params);
        String sign = MD5Util.MD5Encode(data); //sign;


        FormBody.Builder builder  = new FormBody.Builder();
        builder.add("params",data == null ? "" : data);
        builder.add("time", spdf.format(new Date()));
        builder.add("source", "20" );
        builder.add("sign", sign == null ? "" : sign );
        builder.add("device_token", Constant.deviceToken);
        builder.add("lang", "zh_CN");
        builder.add("user_token", Constant.userToken );

        Request.Builder requestBulider = new Request.Builder()
                .url(url);
//                .post(builder.build())
        if(method == Method.POST){

//            requestBulider.post(RequestBody.create(
//                    MediaType.parse("application/json; charset=utf-8"),
//                    json));
            requestBulider.post(builder.build());
        } else if(method == Method.PUT){
            requestBulider.put(builder.build());
        }
        Call call = okHttpClient.newCall(requestBulider.build());
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call,response);
            }
        });
    }


    private static String getJsonParams(Map<String, Object> params){
        if(params == null)
            return null;
        JSONObject obj = new JSONObject();
        Set<String> keySets =  params.keySet();
        for (String key : keySets){
            obj.put(key, params.get(key));
        }
        String json = obj.toString();
        obj.clear();
        return json;
    }

    private static String baseEncodeJson(String json){
        if(json == null)
            return null;
        return new String(new Base64().encode(json.getBytes()));

    }



}
