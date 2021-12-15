package com.example.androidthirdclass.level2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidthirdclass.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonTest extends AppCompatActivity implements View.OnClickListener {
    private List<String> mNameList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Button mbtn_json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_test);
        init();
        JsonDecode();
        mbtn_json.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_JsonTest:
                initRecycle();
                break;
        }
    }

    private void init() {
        mRecyclerView = findViewById(R.id.json_Recycle);
        mbtn_json = findViewById(R.id.button_JsonTest);
    }
    void initRecycle(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter myAdapter = new MyAdapter();
        mRecyclerView.setAdapter(myAdapter);
//        DividerItemDecoration mDivider = new DividerItemDecoration(JsonTest.this,DividerItemDecoration.VERTICAL);
//        mRecyclerView.addItemDecoration(mDivider);
    }


    void JsonDecode(){
        String json = "{\"data\":[{\"id\":6,\"link\":\"\",\"name\":\"面试\",\"order\":1,\"visible\":1},{\"id\":9,\"link\":\"\",\"name\":\"Studio3\",\"order\":1,\"visible\":1},{\"id\":5,\"link\":\"\",\"name\":\"动画\",\"order\":2,\"visible\":1},{\"id\":1,\"link\":\"\",\"name\":\"自定义View\",\"order\":3,\"visible\":1},{\"id\":2,\"link\":\"\",\"name\":\"性能优化 速度\",\"order\":4,\"visible\":1},{\"id\":3,\"link\":\"\",\"name\":\"gradle\",\"order\":5,\"visible\":1},{\"id\":4,\"link\":\"\",\"name\":\"Camera 相机\",\"order\":6,\"visible\":1},{\"id\":7,\"link\":\"\",\"name\":\"代码混淆 安全\",\"order\":7,\"visible\":1},{\"id\":8,\"link\":\"\",\"name\":\"逆向 加固\",\"order\":8,\"visible\":1}],\"errorCode\":0,\"errorMsg\":\"\"}";

        try {
            JSONObject jsonObject = new JSONObject(json);
//            JSONObject jsonObjectName = jsonObject.getJSONObject(json);
//            JSONObject jsonObjectName = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonName = jsonArray.getJSONObject(i);
                String name = jsonName.getString("name");
                mNameList.add(name);
                Log.d("ggg",name);
//                Toast.makeText(JsonTest.this, name, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder>{

        class Holder extends RecyclerView.ViewHolder{
            TextView name;
            public Holder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.json_name);
            }
        }
        @NonNull
        @Override
        public MyAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.json_item,parent,false);
            View view = View.inflate(JsonTest.this,R.layout.json_item,null);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.Holder holder, int position) {
            String name = mNameList.get(position);
            holder.name.setText(name);
        }

        @Override
        public int getItemCount() {
            return mNameList.size();
        }
    }
}