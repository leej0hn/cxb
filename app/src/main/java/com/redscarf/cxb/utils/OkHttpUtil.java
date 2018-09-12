package com.redscarf.cxb.utils;

import android.util.Log;

import com.redscarf.cxb.config.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * <p>function:
 * <p>User: leejohn
 * <p>Date: 17/3/13
 * <p>Version: 1.0
 */
public class OkHttpUtil {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3,TimeUnit.SECONDS).build();

    public static String get(String url)  {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return result(request);
    }

    public static void downloadfile(String url,String savePath)  {
        try {

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            InputStream inputStream = response.body().byteStream();
            byte[] data = new byte[1024];
            int len = 0;
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(savePath);
                while ((len = inputStream.read(data)) != -1) {
                    fileOutputStream.write(data, 0, len);
                }
            } catch (IOException e) {
                Log.e(Config.TAG,e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e(Config.TAG,e.getMessage());
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        Log.e(Config.TAG,e.getMessage());
                    }
                }
            }
        }catch (Exception e){
            Log.e(Config.TAG,e.getMessage());
        }
    }

    public static String upload(File file, String uploadUrl) throws IOException {
        RequestBody fileBody = RequestBody.create(MediaType.parse("text/xml "), file);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(requestBody)
                .build();
        Response response;
        response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String postJson(String url, String json)  {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return result(request);
    }

    public static String postForm(String url, Map<String,String> params, Headers headers){
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .headers(headers)
                .build();
        return result(request);
    }

    public static String postForm(String url,Map<String,String> params,String cookie){
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .addHeader("Cookie",cookie)
                .build();
        return result(request);
    }

    public static String postForm(String url,Map<String,String> params){
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        return result(request);
    }

    private static String result(Request request){
        String resutl = null;
        try{
            Response response = client.newCall(request).execute();
            resutl = response.body().string();
        }catch (IOException ioe){
            Log.e(Config.TAG,ioe.getMessage());
        }
        return resutl;
    }

}
