package com.example.user.dreamtreeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PostView extends AppCompatActivity {

    Button back;
    TextView title,content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        setTitle("정보 상세보기");

        back = (Button)findViewById(R.id.back_1);
        title = (TextView)findViewById(R.id.title);
        content = (TextView)findViewById(R.id.content);
        Intent intent = getIntent();
        Post post = (Post) intent.getSerializableExtra("PostInfo");

        title.setText(post.getTitle());
        content.setText(post.getContent());

    }
    public void onClick(View v){
        Intent intent = getIntent();
        Post post = (Post) intent.getSerializableExtra("PostInfo");
        switch (v.getId()){
            case R.id.back_1:
                finish();
                break;
        }

    }
}