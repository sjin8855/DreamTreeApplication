package com.example.user.dreamtreeapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewPost extends AppCompatActivity {

    private String ip = "61.255.4.166";//IP
    public static int SERVERPORT = 7777;

    MyAsyncTask myAsyncTask;
    static DataOutputStream Dout;
    static DataInputStream Din;
    Socket mSocket;

    EditText ettitle,etcontent;
    Post post1= new Post();

    boolean writeDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ettitle = (EditText)findViewById(R.id.ettitle);
        etcontent = (EditText)findViewById(R.id.etcontent);


        setTitle("New Post");

    }

    public void onClick(View v){
        if(v.getId()==R.id.back){
            finish();
        }
        else{
            post1.setTitle(ettitle.getText().toString());
            post1.setContent(etcontent.getText().toString());
            post1.setDate(finddate());
            Intent intent = getIntent();
            intent.putExtra("result",post1);    //post를 첨부한다.
            setResult(RESULT_OK,intent);

            MyAsyncTask myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
        }
    }

    public String finddate()
    {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String fmdate = sdf.format(date);
        return fmdate;
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
                if(line.contains("CommunityListUpdated__"))
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
                finish();

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
                Dout.writeUTF("writePostInfo_:"
                        + "Post!@LoginedUser:" + Login.loginedUser.get_ID()
                        + ":Post!@Title:" + post1.getTitle().toString()
                        + ":Post!@Content:" + post1.getContent().toString()
                        + ":Post!@Date:" + post1.getDate().toString());

                System.out.println("writePostInfo_:"
                        + "Post!@LoginedUser:" + Login.loginedUser.get_ID()
                        + ":Post!@Title:" + post1.getTitle().toString()
                        + ":Post!@Content:" + post1.getContent().toString()
                        + ":Post!@Date:" + post1.getDate().toString());
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
