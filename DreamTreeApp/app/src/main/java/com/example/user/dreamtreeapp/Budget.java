package com.example.user.dreamtreeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Budget extends AppCompatActivity {

    TextView DateText;
    TextView Foodtext;
    TextView SpendMoneytext;
    TextView memoText;

    String year;
    String month;
    String dayOfMonth;
    String spendMoney;
    String category;
    String memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        DateText = (TextView) findViewById(R.id.DateText);
        Foodtext = (TextView) findViewById(R.id.Foodtext);
        SpendMoneytext = (TextView) findViewById(R.id.SpendMoneytext);
        memoText = (TextView) findViewById(R.id.memoText);
    }

    public void onInputBudgetButtonClicked(View v)
    {
        Intent intent = new Intent(getApplicationContext(),InputBudget.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==1)
        {
            if(resultCode == RESULT_OK)
            {
                String result = data.getStringExtra("result");
                year = data.getStringExtra("year");
                month = data.getStringExtra("month");
                dayOfMonth = data.getStringExtra("day");
                spendMoney = data.getStringExtra("spendMoney");
                category = data.getStringExtra("category");
                memo = data.getStringExtra("memo");

                DateText.setText(year + "." + month + "." + dayOfMonth);
                Foodtext.setText(category);
                SpendMoneytext.setText(spendMoney);
                memoText.setText(memo);
            }
        }
    }
}
