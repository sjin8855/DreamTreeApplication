package com.example.user.dreamtreeapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
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
import java.util.List;

public class Budget extends AppCompatActivity {

    TextView DateText;
    TextView Foodtext;
    TextView SpendMoneytext;
    TextView memoText;
    TextView AlarmText;

    BudgetInfo newBudget;
    List<BudgetInfo> budgetList;
    LinearLayout root;
    TextView t[];

    private String ip = "61.255.4.166";//IP
    public static int SERVERPORT = 7777;

    MyAsyncTask myAsyncTask;
    static DataOutputStream Dout;
    static DataInputStream Din;
    Socket mSocket;
    static ObjectInputStream Oin;


    String year;
    String month;
    String dayOfMonth;
    String spendMoney;
    String category;
    String memo;

    boolean BudgetInfoLoadingDone;
    boolean isInDanger = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        BudgetInfoLoadingDone = false;
        AlarmText = (TextView) findViewById(R.id.AlarmText);

        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

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
                try
                {
                    budgetList  = new ArrayList<BudgetInfo>();
                    Oin = new ObjectInputStream(mSocket.getInputStream());
                    budgetList = (ArrayList<BudgetInfo>) Oin.readObject();

                    line = Oin.readUTF();
                    if(line.contains("ThisChildIsInDanger!@!@"))
                    {
                        System.out.println("ThisChildIsInDanger!@!@!!!!!!!!!!!!!!!");
                        isInDanger = true;
                    }
                    else
                    {
                        System.out.println("NoDanger!@!@!!!!!!!!!!!!!!!");
                        isInDanger = false;
                    }
                    Oin.close();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }

                for(int i=0;i<budgetList.size();i++)
                {
                    System.out.println(budgetList.get(i).getMemo() + " :::::: " + budgetList.size());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
            {
                if(!BudgetInfoLoadingDone)
                {
                    printBudgetList();
                }

                if(isInDanger)
                {
                    AlarmText.setText("편의점 말고 다른곳도 가세요!");
                    AlarmText.setTextColor(Color.rgb(0,0,0));
                }
                else
                {
                    AlarmText.setText("오늘도 좋은 하루 보내세요!");
                    AlarmText.setTextColor(Color.rgb(0,0,0));
                }

                mSocket.close();
                System.out.println("Socket closed");

                BudgetInfoLoadingDone = true;
            } catch (IOException e)
            {
                e.printStackTrace();
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
                if(!BudgetInfoLoadingDone)
                {
                    Dout.writeUTF("GiveMeTheBudgetData_:" + Login.loginedUser.get_ID());
                    Dout.flush();
                }


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

    public void printBudgetList()
    {
        root = (LinearLayout) findViewById(R.id.rl);
        root.setVerticalGravity(Gravity.BOTTOM);
        t = new TextView[100];
        LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        for(int i=0;i<budgetList.size();i++)
        {
            t[i] = new TextView(getApplicationContext());
            t[i].setLayoutParams(dim);
            t[i].setText(budgetList.get(i).getDate() + "에 " + budgetList.get(i).getCategory() + "를 " + budgetList.get(i).getSpendMoney() +"원을 내고 먹었습니다"
                    + "\n" + "메모: " + budgetList.get(i).getMemo());
            t[i].setTextSize(15);
            t[i].setTextColor(Color.BLACK);
            t[i].setGravity(Gravity.CENTER);
            t[i].setPadding(16, 16, 16, 16);
            root.addView(t[i]);
        }
    }
}
