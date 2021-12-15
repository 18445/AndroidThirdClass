package com.example.androidthirdclass.mainclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidthirdclass.R;
import com.example.androidthirdclass.level2.JsonTest;
import com.example.androidthirdclass.level3.RegisterTest;
import com.example.androidthirdclass.level4.GlideTest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtn_level2;
    private Button mBtn_level3;
    private Button mBtn_level4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mBtn_level2.setOnClickListener(this);
        mBtn_level3.setOnClickListener(this);
        mBtn_level4.setOnClickListener(this);
    }

    void init(){
        mBtn_level2 = findViewById(R.id.button1);
        mBtn_level3 = findViewById(R.id.button2);
        mBtn_level4 = findViewById(R.id.button3);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.button1:
                Intent intent2 = new Intent(MainActivity.this, JsonTest.class);
                startActivity(intent2);
                break;
            case R.id.button2:
                Intent intent3 = new Intent(MainActivity.this, RegisterTest.class);
                startActivity(intent3);
                break;
            case R.id.button3:
                Intent intent4 = new Intent(MainActivity.this, GlideTest.class);
                startActivity(intent4);
                break;

        }
    }

}