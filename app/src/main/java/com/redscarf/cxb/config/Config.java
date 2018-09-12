package com.redscarf.cxb.config;

/**
 * @author LeeJohn
 * @date 2018/09/12
 * @description:
 */
public class Config {
    public static String TAG = "cxb";

    private static String DOMAIN = "https://286.com";
    /**
     * 登录URL
     */
    public static String loginUrl = DOMAIN + "/Account/Login";
    /**
     * 加载数据URL
     */
    public static String loadDataUrl = DOMAIN + "/KenoMatch/LoadData/1";
    /**
     * 提交订单URL
     */
    public static String batchPostUrl = DOMAIN + "/KenoBet/BatchPost";

}
