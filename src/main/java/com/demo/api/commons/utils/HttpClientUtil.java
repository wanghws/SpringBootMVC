package com.demo.api.commons.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghw on 2019-03-15.
 */
@Slf4j
public class HttpClientUtil {
    private static OkHttpClient client = new OkHttpClient()
            .newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true).build();

    public static String get(String url,String headerKey,String headerValue) throws Exception{
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Connection","close")
                .addHeader(headerKey,headerValue)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String get(String url) throws Exception{
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Connection","close")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public static String post(String url,String params) throws Exception{
        RequestBody body = new FormBody.Builder().add("params",params).build();

        return post(url,body);
    }
    public static String postParams(String url, Map<String,String> params) throws Exception{
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String,String> entry: params.entrySet()){
            builder.add(entry.getKey(),entry.getValue());
            log.info("params:"+entry.getKey()+" "+entry.getValue());
        }
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public static String postBody(String url,String params,String token) throws Exception{
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),params);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Connection","close")
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public static String postBody(String url,String params) throws Exception{
        RequestBody body = RequestBody.create(MediaType.parse("text/xml; charset=utf-8"),params);
        return post(url,body);
    }
    public static String post(String url,RequestBody body)throws Exception{
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Connection","close")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String getGithub(String url,String githubToken) throws Exception{
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent","Mozilla/5.0")
                .addHeader("Authorization","token "+githubToken)
                .addHeader("Content-Type","application/json")
                .addHeader("Accept","application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
