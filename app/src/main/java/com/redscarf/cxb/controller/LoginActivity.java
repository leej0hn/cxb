package com.redscarf.cxb.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.redscarf.cxb.R;
import com.redscarf.cxb.config.AppJsonConfig;
import com.redscarf.cxb.config.Config;
import com.redscarf.cxb.utils.CxbHttpUtils;
import com.redscarf.cxb.utils.JsonFileUtils;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText accountEditText;
    private EditText passwordEditText;

    private Button loginButton;
    private Button batchPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setView();
        setListener();
    }

    private void setView() {
        accountEditText = findViewById(R.id.account);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);
        batchPostButton = findViewById(R.id.btnBatchPost);

        setValue();
    }

    private void setValue() {

        String account = JsonFileUtils.get(AppJsonConfig.wxboy_account);
        String passwod = JsonFileUtils.get(AppJsonConfig.wxboy_password);

        if (!TextUtils.isEmpty(account)) {
            accountEditText.setText(account);
        }
        if (!TextUtils.isEmpty(passwod)) {
            passwordEditText.setText(passwod);
        }
    }


    private void setListener() {
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            String userName = accountEditText.getText().toString();
                            String password = passwordEditText.getText().toString();
                            boolean login = CxbHttpUtils.login(userName, password);
                            if (login) {
//                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG);
                                Log.i(Config.TAG,"登录成功");
                            } else {
                                Log.i(Config.TAG,"登录失败");
//                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG);
                            }
                        }catch (Exception e){
                            Log.e(Config.TAG,"LoginActivity.setListener.loginButton.onClick.Thread.run : " + e.getMessage());
                        }
                    }
                }.start();
            }
        });

        batchPostButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Map<String, String> params = new HashMap<>();
                            params.put("kenoId", "1");
                            params.put("cart[0][playId]", "1"); //第一注
                            params.put("cart[0][dtype]", "1"); //玩法类型
                            params.put("cart[0][content]", "");
                            params.put("cart[0][isComplex]", "false"); //是否混合
                            params.put("cart[0][pl]", "1.99"); //赔率
                            params.put("cart[0][money]", "1"); //金额
                            boolean batchPost = CxbHttpUtils.batchPost(params);
                            if (batchPost) {
                                Log.i(Config.TAG, "订单提交成功");
                            } else {
                                Log.i(Config.TAG, "订单提交失败");
                            }
                        } catch (Exception e) {
                            Log.e(Config.TAG, "LoginActivity.setListener.batchPostButton.onClick.Thread.run : " + e.getMessage());
                        }
                    }
                }.start();
            }
        });
    }

}
