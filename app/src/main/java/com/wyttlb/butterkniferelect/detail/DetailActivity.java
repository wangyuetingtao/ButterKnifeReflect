package com.wyttlb.butterkniferelect.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wyttlb.butter_knife_annotation.BindView;
import com.wyttlb.butter_knife_reflect.ButterKnifeReflect;
import com.wyttlb.butterkniferelect.R;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.btn_back)
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnifeReflect.bind(this);
        tvDetail.setText("这是详情页");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}