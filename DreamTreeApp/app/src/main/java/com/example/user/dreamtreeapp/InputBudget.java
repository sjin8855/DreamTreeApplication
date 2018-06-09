package com.example.user.dreamtreeapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class InputBudget extends AppCompatActivity {

    TextView txtText;

    Button selectDate;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    EditText spendMoney;

    Button category;

    EditText memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_input_budget);

        selectDate = (Button) findViewById(R.id.DatePickerButton);
        spendMoney = (EditText) findViewById(R.id.spendMoney);
        category = (Button) findViewById(R.id.categoryButton);
        memo = (EditText) findViewById(R.id.memo);

        txtText = (TextView) findViewById(R.id.txttext);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        txtText.setText(data);
    }

    public void mOnClose(View v)
    {
        Intent intent = new Intent();
        intent.putExtra("year", String.valueOf(year));
        intent.putExtra("month", String.valueOf(month));
        intent.putExtra("day", String.valueOf(dayOfMonth));
        intent.putExtra("spendMoney", spendMoney.getText().toString());
        intent.putExtra("category", category.getText());
        intent.putExtra("memo", memo.getText().toString());
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

    public void onClickDatePickerButton(View view)
    {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(InputBudget.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                selectDate.setText(day + "/" + month + "/" + year);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    //안드로이드 백버튼 비활성화
    public void onBackPressed()
    {
        //return;
    }

    public void onClickCategoryButton(View v)
    {
        Intent intent = new Intent(getApplicationContext(),CategoryList.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1)
        {
            if(resultCode == RESULT_OK)
            {
                String result = data.getStringExtra("category");
                category.setText(result);
            }
        }
    }
}
