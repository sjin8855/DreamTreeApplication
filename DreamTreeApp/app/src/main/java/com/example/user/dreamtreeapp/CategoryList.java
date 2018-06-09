package com.example.user.dreamtreeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class CategoryList extends AppCompatActivity {

    String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_category_list);
    }

    public void mOnClose(View v)
    {
        Intent intent = new Intent();
        intent.putExtra("category", category);
        setResult(RESULT_OK,intent);

        finish();
    }
    //화면 바깥쪽눌렀을때 닫히는거 막도록
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE)
        {
            return false;
        }
        return true;
    }

    public void KoreanFood(View v)
    {
        category = "한식";
        mOnClose(v);
    }
    public void ChineseFood(View v)
    {
        category = "중식";
        mOnClose(v);
    }
    public void JapaneseFood(View v)
    {
        category = "일식";
        mOnClose(v);
    }
    public void ConvenientFood(View v)
    {
        category = "편의점";
        mOnClose(v);
    }

}
