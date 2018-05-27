package com.example.user.dreamtreeapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Splash extends AppCompatActivity {

    private String ip = "61.255.4.166";//IP
    public static int SERVERPORT = 7777;

    MyAsyncTask myAsyncTask;
    static DataOutputStream Dout;
    static DataInputStream Din;
    Socket mSocket;

    static ObjectOutputStream Oout;
    static ObjectInputStream Oin;

    List<Store> storeList = null;

    StoreManager sm;

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sm = new StoreManager();

        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent mainIntent = new Intent(Splash.this,Menu.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
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
                    System.out.println("받ㅂ다ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅡㅡㅡㅡㅡㅡ");
                    storeList  = new ArrayList<Store>();
                    Oin = new ObjectInputStream(mSocket.getInputStream());
                    storeList = (ArrayList<Store>) Oin.readObject();
                    Oin.close();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                System.out.println("싸이즈ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
                System.out.println(storeList.size());
                System.out.println("싸이즈ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

                /*
                for(int i=0;i<storeList.size();i++)
                {
                    String name = storeList.get(i).get_Name();
                    String boroughName = storeList.get(i).get_BoroughName();

                    System.out.println("name: " + name + " boroughName: " + boroughName);
                }
                */

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
                sm.setStoreList(storeList);
                mSocket.close();
                System.out.println("Socket closed");

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
                Dout.writeUTF("GetStoreInfo!!@@");
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
