package com.example.androidthirdclass.level3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Register extends AppCompatActivity {
    private Button mBtnRegister;
    private EditText mEtAccount;
    private EditText mEtPassword;
    private EditText mEtRePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        init();
        mBtnRegister.setOnClickListener(v -> {
            String account = mEtAccount.getText().toString();
            String password = mEtPassword.getText().toString();
            String rePassword = mEtRePassword.getText().toString();
            isRegister(account,password,rePassword);
        });
    }

    private void init() {
        mBtnRegister = findViewById(R.id.button_register_register);
        mEtAccount = findViewById(R.id.edit_register_account);
        mEtPassword = findViewById(R.id.edit_register_password1);
        mEtRePassword = findViewById(R.id.edit_register_password2);
    }

    private boolean isRepeat(String password1,String password2){
        return password1.equals(password2);
    }

    void isRegister(String account,String password,String rePassword){
//        HashMap<String,String> map = new HashMap<>();
//        map.put(account,password);
        sendNetRequest("https://www.wanandroid.com/user/register",account,password,rePassword);
//        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();

    }

//    private void sendNetRequest(String mUrl, HashMap<String,String> params){
    private void sendNetRequest(String mUrl, String account,String password,String rePassword){
        new Thread(
            ()->{
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(mUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    String dataToWrite = "username="+account+"&password="+password+"&repassword="+rePassword;
                    connection.connect();
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(dataToWrite.getBytes());
                    InputStream in = connection.getInputStream();
                    String responseData = StreamToString(in);
                    Log.d("ggg",responseData);
                    jsonDecode(responseData, new NetResult() {
                        @Override
                        public void onSuccess(String str) {
                            Looper.prepare();
                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        @Override
                        public void onError(String e) {
                            Looper.prepare();
                            Toast.makeText(Register.this,e,Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    });
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

    private void jsonDecode(String json,NetResult netResult){
        try {
            JSONObject jsonObject = new JSONObject(json);
            int jsonErrorCode = jsonObject.getInt("errorCode");
            Log.d("ggg", String.valueOf(jsonErrorCode));
            String jsonMsg = jsonObject.getString("errorMsg");
            Log.d("ggg", jsonMsg);
            if(jsonErrorCode == 0){
                netResult.onError(jsonMsg);
                Log.d("ggg","error");
            }else {
                netResult.onSuccess(jsonMsg);
                Log.d("ggg","success");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private String StreamToString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
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