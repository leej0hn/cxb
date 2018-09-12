package com.redscarf.cxb.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.redscarf.cxb.config.AppJsonConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>function: 用于应用间共享数据，保存Json格式数据，基于文件操作工具类。
 * <p>User: LeeJohn
 * <p>Date: 2017/11/13
 * <p>Version: 1.0
 */
public class JsonFileUtils {
    private static Map<String,String> configMap ;

    public static void put(String path , String fileNamePath ,Map<String,String> params){
        try {
            String jsonStr = "";
            File file = createFile(path,fileNamePath);
            jsonStr = FileUtils.readString(file);
            parseMap(jsonStr);
            if( params != null ){
                configMap.putAll(params);
            }
            jsonStr = JSONObject.toJSONString(configMap);
            FileUtils.writeString(jsonStr,file);
            updateMap();
        }catch (Exception e){
            Log.e("Xposed","JsonFileUtils.put : " + e.getMessage() );
        }
    }

    public static void put(Map<String,String> params){
        put(AppJsonConfig.app_config_path, AppJsonConfig.app_config_file_path,params);
    }

    public static String get(String key){
        String value = "";
        try{
            updateMap();
            value = configMap.get(key);
        }catch (Exception e){
            Log.e("Xposed","JsonFileUtils.get : " + e.getMessage());
        }
        return value;
    }

    public static String getfile(String key){
        String value = "";
        try{
            updateMap();
            value = configMap.get(key);
        }catch (Exception e){
            Log.e("Xposed","JsonFileUtils.getfile : " + e.getMessage());
        }
        return value;
    }

    private static void updateMap(){
        File file = new File(AppJsonConfig.app_config_file_path);
        if( !file.exists() ){
            file = createFile(AppJsonConfig.app_config_path, AppJsonConfig.app_config_file_path);
        }
        String jsonStr = FileUtils.readString(file);
        parseMap(jsonStr);

    }

    private static void parseMap(String jsonStr){
        try {
            configMap = JSONObject.parseObject(jsonStr,Map.class);
        }catch (Exception e){
//            Log.w("Xposed","JsonFileUtils.parseMap : " + e.getMessage());
        }finally {
            if( configMap == null ){
                configMap = new HashMap<>();
            }
        }
    }

    private static File createFile(String path , String fileNamePath){
        FileUtils.createIsNotExists(path);
        File file = new File(fileNamePath);
        try {
            //创建文件
            if (!file.exists()) {
                file.createNewFile();
            }
        }catch (Exception e){
            Log.e("Xposed","JsonFileUtils.createFile : " + e.getMessage() );
        }
        return file;
    }
}
