package com.example.androidthirdclass.level4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.androidthirdclass.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;



public class GlideTest extends AppCompatActivity {
    private ViewPager2 mViewPager2;
    List<String> mListImage = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_test);
        init();
//      异常？？？？
//        getView();
        JsonDecode("{\"data\":[{\"desc\":\"一起来做个App吧\",\"id\":10,\"imagePath\":\"https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png\",\"isVisible\":1,\"order\":1,\"title\":\"一起来做个App吧\",\"type\":0,\"url\":\"https://www.wanandroid.com/blog/show/2\"},{\"desc\":\"\",\"id\":6,\"imagePath\":\"https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png\",\"isVisible\":1,\"order\":1,\"title\":\"我们新增了一个常用导航Tab~\",\"type\":1,\"url\":\"https://www.wanandroid.com/navi\"},{\"desc\":\"\",\"id\":20,\"imagePath\":\"https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png\",\"isVisible\":1,\"order\":2,\"title\":\"flutter 中文社区 \",\"type\":1,\"url\":\"https://flutter.cn/\"}],\"errorCode\":0,\"errorMsg\":\"\"}");
        mViewPager2.setAdapter(new ViewPagerAdapter());
    }
    private String StreamToString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        while (true) {
            try {
                if (((line = reader.readLine()) == null)) break;
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

    private void getView() {
        String mUrl = "https://www.wanandroid.com/banner/json";
        new Thread(
            ()->{
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(mUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
                    connection.setRequestProperty("Accept-Encoding","gzip,deflate");
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    String response = StreamToString(in);
                    Log.d("ggg",response);
                    JsonDecode(response);
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
    private void JsonDecode(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonName = jsonArray.getJSONObject(i);
                String address = jsonName.getString("imagePath");
                mListImage.add(address);
                Log.d("ggg",address);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void init() {
        mViewPager2 = findViewById(R.id.viewPager2);
    }

    class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView ;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.viewpager_image);
            }
        }
        @NonNull
        @Override
        public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_item,parent,false);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(lp);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
            String url = mListImage.get(position);
            Log.d("ggg", "(" + position + "):URL)-->> " + url);
            Glide.with(holder.imageView)
                    .load(url)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mListImage.size();
        }
    }

}