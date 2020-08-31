package com.wyttlb.butterkniferelect.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wyttlb.butter_knife_annotation.BindView;
import com.wyttlb.butter_knife_reflect.ButterKnifeReflect;
import com.wyttlb.butterkniferelect.R;
import com.wyttlb.butterkniferelect.detail.DetailActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_text)
    TextView textView;
    @BindView(R.id.btn_go)
    Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnifeReflect.bind(this);

        textView.setText("ButterKnife Reflect");
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DetailActivity.class));
            }
        });
    }
}