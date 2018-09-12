package com.redscarf.cxb;

import com.alibaba.fastjson.JSON;
import com.redscarf.cxb.utils.OkHttpUtil;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public static String URL = "https://286.com";

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void login() throws Exception{
        System.out.println("test login...");
        String loginUrl = URL + "/Account/Login";
        System.out.println("url : " + loginUrl);
        Map<String,String> params = new HashMap<>();
        params.put("userName","abc1234567");
        params.put("passwd","a123456");
        params.put("validCode","");
        String result = OkHttpUtil.postForm(loginUrl, params);
        System.out.println(result);
        String ticket = JSON.parseObject(result).get("ticket").toString();
        System.out.println("ticket : " + ticket);
        loadData(ticket);
    }

    private void loadData(String ticket) throws Exception{
        String loadUrl = URL + "/KenoMatch/LoadData/1";
        System.out.println("url : " + loadUrl);
        String result = OkHttpUtil.get(loadUrl);
        System.out.println(result);
        String info = JSON.parseObject(result).get("info").toString();
        String matchId = JSON.parseObject(info).get("matchId").toString();
        System.out.println(matchId);
        post(ticket,matchId);
    }

    private void post(String ticket,String matchId) throws Exception{
        String postUrl = URL + "/KenoBet/BatchPost";
        System.out.println("url : " + postUrl);

        String cookie = "ticket=" + ticket;

        Map<String,String> params = new HashMap<>();
        params.put("kenoId","1");
        params.put("matchId",matchId);
        params.put("cart[0][playId]","1");
        params.put("cart[0][dtype]","1");
        params.put("cart[0][content]","");
        params.put("cart[0][isComplex]","false");
        params.put("cart[0][pl]","1.99");
        params.put("cart[0][money]","1");

        String result = OkHttpUtil.postForm(postUrl, params,cookie);
        System.out.println(result);

    }

}