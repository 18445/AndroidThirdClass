package com.example.androidthirdclass.level3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidthirdclass.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class RegisterTest extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnLogin;
    private Button mBtnRegister;
    private EditText mETAccount;
    private EditText mETPassword;
    private String cookieString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        init();
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        StringBuilder cookieBuilder = new StringBuilder();
        String divider = "";
//        Cookie[] cookies = request.getCookies();
//        for (HttpCookie cookie : getCookies()) {
//            cookieBuilder.append(divider);
//            divider = ";";
//            cookieBuilder.append(cookie.getName());
//            cookieBuilder.append("=");
//            cookieBuilder.append(cookie.getValue());
//        }
        cookieString = cookieBuilder.toString();
    }

    private void init() {
        mBtnLogin = findViewById(R.id.button_login);
        mBtnRegister = findViewById(R.id.button_register);
        mETAccount = findViewById(R.id.edit_account);
        mETPassword = findViewById(R.id.edit_password);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_login:
                String account = mETAccount.getText().toString().trim();
                String password = mETPassword.getText().toString().trim();
//                if(!isEmpty(account,password)){
//                    Toast.makeText(this, "账户或密码不能为空", Toast.LENGTH_SHORT).show();
//                    break;
//                }
                sendNetRequest("https://www.wanandroid.com/user/login",account,password);
                break;
            case R.id.button_register:
                Intent register = new Intent(this,Register.class);
                startActivity(register);
                break;
        }
    }

    private boolean isEmpty(String account,String password){
        if(account.length() == 0 || password.length() == 0){
            return false;
        }
        return true;
    }

    private void isLogin(String jsonData,String user_account,String user_password){
        try {
            if(TextUtils.isEmpty(jsonData)){
                return;
            }
            JSONObject jsonObject = new JSONObject(jsonData);
//            JSONObject jsonAccount = jsonObject.getJSONObject("username");
//            JSONObject jsonPassword = jsonObject.getJSONObject("password");
            JSONArray jsonAccount = jsonObject.getJSONArray("username");
            JSONArray jsonPassword = jsonObject.getJSONArray("password");
            for (int i = 0; i < jsonAccount.length(); i++) {
                JSONObject json_account = jsonAccount.getJSONObject(i);
                String account = json_account.getString("username");
                if(account.equals(user_account)){
                    JSONObject json_password = jsonPassword.getJSONObject(i);
                    String password = json_password.getString("password");
                    if(password.equals(user_password)){
                        success();
                        return ;
                    }
                }
                Toast.makeText(this, "登录失败，账号或密码错误", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void success() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
    }

    private void sendNetRequest(String mUrl,String account,String password){
        new Thread(
                ()->{
                    HttpURLConnection connection = null;
                    try {
                        URL url = new URL(mUrl);
                        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
                        connection.setRequestProperty("Accept-Encoding","gzip,deflate");
                        Cookie:connection.setRequestProperty("Cookie",cookieString);
                        connection.setUseCaches(false);
                        Cookie:connection.getHeaderField("Set-Cookie");
                        connection.connect();
                        InputStream in = connection.getInputStream();
                        String responseData = StreamToString(in);
                        Log.d("ggg","(connect)-->> success");
                        jsonDecode(responseData, new NetResult() {
                            @Override
                            public void onSuccess(String str) {
                                Looper.prepare();
                                Toast.makeText(RegisterTest.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                            @Override
                            public void onError(String e) {
                                Looper.prepare();
                                Toast.makeText(RegisterTest.this, e, Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        });
//                        isLogin(responseData,account,password);,
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(connection != null){
                            connection.disconnect();
                        }
                    }
                }
        ).start();
    }
//
//    private void sendNetRequest(String mUrl, HashMap<String,String> params){
//        ((Runnable) () -> {
//            try {
//                URL url = new URL(mUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setConnectTimeout(8000);
//                connection.setReadTimeout(8000);
//                connection.setDoOutput(true);
//                connection.setDoInput(true);
//                StringBuilder dataToWrite = new StringBuilder();
//                for (String key : params.keySet()) {
//                    dataToWrite.append(key).append("=").append(params.get(key)).append("&");
//                }
//                connection.connect();
//                OutputStream outputStream = connection.getOutputStream();
//                outputStream.write(dataToWrite.substring(0, dataToWrite.length() - 1).getBytes(StandardCharsets.UTF_8));
//                InputStream in = connection.getInputStream();
//                String responseData = StreamToString(in);
//                jsonDecode(responseData);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).run();
//    }

    private void jsonDecode(String json,NetResult netResult){
        try {
            JSONObject jsonObject = new JSONObject(json);
//            JSONArray jsonArray = jsonObject.getJSONArray("data");
            int jsonErrorCode = jsonObject.getInt("errorCode");
            String jsonMsg = jsonObject.getString("errorMsg");
            if(jsonErrorCode == 0){
                netResult.onError(jsonMsg);
            }else {
                netResult.onSuccess(jsonMsg);
//                Toast.makeText(RegisterTest.this, jsonMsg, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private String StreamToString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//        while (true) {
//                if (((line = reader.readLine()) == null)) break;
//                sb.append(line).append('\n');
            try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//        }
        System.out.println(sb.toString());
        return sb.toString();
    }


}