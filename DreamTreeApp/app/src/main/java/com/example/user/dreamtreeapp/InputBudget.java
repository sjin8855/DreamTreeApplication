package com.example.user.dreamtreeapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

public class InputBudget extends AppCompatActivity {

    TextView txtText;

    private String ip = "61.255.4.166";//IP
    public static int SERVERPORT = 7777;

    MyAsyncTask myAsyncTask;
    static DataOutputStream Dout;
    static DataInputStream Din;
    Socket mSocket;

    Button selectDate;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    EditText spendMoney;

    Button category;

    EditText memo;

    boolean writeDone = false;

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
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

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
        }, year, month+1, dayOfMonth);
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

    private class MyAsyncTask extends AsyncTask<Void,Void,Void>
    {
        protected void onPreExecute()
        {
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                mSocket = new Socket(ip,SERVERPORT);
                Dout = new DataOutputStream(mSocket.getOutputStream());
                Din = new DataInputStream(mSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("서버에 연결되었습니다");
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            final Sender messageSender = new Sender(); // Initialize chat sender
            // AsyncTask.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else {
                messageSender.execute();
            }
            Receiver receiver = new Receiver();
            receiver.execute();
        }
    }

    private class Receiver extends AsyncTask<Void,Void,Void>
    {
        protected void onPreExecute()
        {
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String line;
            try
            {
                line = Din.readUTF();
                System.out.println(line);
                if(line.contains("BudgetListUpdated__"))
                {
                    writeDone = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if(writeDone)
            {
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),Budget.class);
                finish();
                startActivityForResult(intent,1);

            }
        }
    }

    private class Sender extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                Dout.writeUTF("writeBudgetInfo_:"
                        + "Budget!@LoginedUser:" + Login.loginedUser.get_ID()
                        + ":Budget!@Category:" + category.getText().toString()
                        + ":Budget!@Expenditure:" + spendMoney.getText().toString()
                        + ":Budget!@MEMO:" + memo.getText().toString()
                        + ":Budget!@SpendDate:" + selectDate.getText().toString());

                System.out.println("writeBudgetInfo_:"
                        + "Budget!@LoginedUser:" + Login.loginedUser.get_ID()
                        + ":Budget!@Category:" + category
                        + ":Budget!@Expenditure:" + spendMoney.getText().toString()
                        + ":Budget!@MEMO:" + memo.getText().toString()
                        + ":Budget!@SpendDate:" + year + "/" + month + "/" + dayOfMonth);
                Dout.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        }
    }
}
