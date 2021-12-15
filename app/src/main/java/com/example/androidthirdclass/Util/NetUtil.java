package com.example.androidthirdclass.Util;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NetUtil {
    public static final int GET = 1;
    public static final int POST = 2;
    public static final int FAIL = -1;
    public static void sendNetRequest(String address,String method,String params,final NetResult netResult){
        new Thread(
                ()->{
                    HttpURLConnection connection = null;
                    int METHOD;
                    if(method.equalsIgnoreCase("GET")){
                        METHOD = 1;
                    }else if(method.equalsIgnoreCase("POST")){
                        METHOD = 2;
                    }else {
                        METHOD = -1;
                    }
                    try {
                        URL url = new URL(address);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod(method);
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        switch(METHOD){
                            case GET:
                                InputStream in = connection.getInputStream();
                                String response = StreamToString(in);
                                netResult.onSuccess(response);
                                break;
                            case POST:
                                OutputStream outputStream = connection.getOutputStream();
                                outputStream.write(params.getBytes(StandardCharsets.UTF_8));
                                break;
                            case FAIL:
                                netResult.onFailMatch(new Throwable());

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        netResult.onError(e);
                    } finally {
                        if(connection != null){
                            connection.disconnect();
                        }
                    }
                }
        ).start();

    }

    interface NetResult{
        void onSuccess(String str);
        void onError(Throwable e);
        void onFailMatch(Throwable e);
    }
    private static String StreamToString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        while (true) {
            try {
                if (((line = reader.readLine()) != null)) break;
                sb.append(line).append('\n');
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}

