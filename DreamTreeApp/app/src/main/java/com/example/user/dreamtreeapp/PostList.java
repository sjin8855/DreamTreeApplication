package com.example.user.dreamtreeapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class PostList extends Activity {

    ListView listView;
    TextView tv;
    ArrayAdapter<String> adapter;
    final ArrayList<String> PostData = new ArrayList<String>();
    final ArrayList<Post> PostList = new ArrayList<Post>();
    final int NEW_POST = 1004;
    Button newpost;
    Post post;


    List<Post> postList;
    private String ip = "61.255.4.166";//IP
    public static int SERVERPORT = 7777;

    MyAsyncTask myAsyncTask;
    static DataOutputStream Dout;
    static DataInputStream Din;
    Socket mSocket;
    static ObjectInputStream Oin;

    boolean postListLoadingDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

/*
        tv = (TextView)findViewById(R.id.tv);
        tv.setText("리스트(" + PostData.size() + "개)");
        setTitle("New Post");
        setListView();
        */

    }
    //버튼 클릭하면 정보 입력 페이지로 넘어가게 된다.
    public void onClick(View v)
    {
        Intent intent = new Intent(PostList.this,NewPost.class);
        intent.putExtra("PostList",PostData);
        startActivityForResult(intent,NEW_POST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == NEW_POST) {
            if (resultCode == RESULT_OK) {
                Post post = (Post) data.getSerializableExtra("result"); //새로운 글을 받아온다.
                PostData.add(post.getTitle());
                PostList.add(post);
                adapter.notifyDataSetChanged();
                tv.setText("리스트(" + PostData.size() + "개)");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setListView(){
        listView = (ListView)findViewById(R.id.listview);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,PostData);
        listView.setAdapter(adapter);
        Log.d("Count---------+111", String.valueOf(listView));
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int position, long l) {
                //정보를 삭제하는지 묻는 대화상자 나타남
                AlertDialog.Builder dlg = new AlertDialog.Builder(view.getContext());
                dlg.setTitle("삭제확인")
                        .setMessage("선택한 글을 삭제하시겠습니까?")
                        .setNegativeButton("취소",null)
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PostData.remove(position);
                                PostList.remove(position);
                                adapter.notifyDataSetChanged();
                                tv.setText("리스트("+ PostData.size()+"개)");
                                Toast.makeText(getApplicationContext(),"굳",Toast.LENGTH_LONG).show();
                            }
                        }).show();
                return true;
            }
        });

        //클릭하면 상세정보 나타내주는 페이지로 넘어가게 하기.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PostList.this,PostView.class);
                Post post = PostList.get(i);
                intent.putExtra("PostInfo",post);
                startActivity(intent);
            }
        });

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
                    postList  = new ArrayList<Post>();
                    Oin = new ObjectInputStream(mSocket.getInputStream());
                    postList = (ArrayList<Post>) Oin.readObject();

                    //line = Oin.readUTF();
                    /*
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
                    */
                    Oin.close();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
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
                if(!postListLoadingDone)
                {
                    //printBudgetList();
                    for(int i=0;i<postList.size();i++)
                    {
                        String Title = postList.get(i).getTitle();
                        String Content = postList.get(i).getTitle();
                        String Date = postList.get(i).getDate();

                        tv = (TextView)findViewById(R.id.tv);
                        tv.setText("리스트(" + PostData.size() + "개)");
                        setTitle("New Post");
                        setListView();
                    }
                    setListView();
                }

                /*
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
                */

                mSocket.close();
                System.out.println("Socket closed");

                postListLoadingDone = true;
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
                if(!postListLoadingDone)
                {
                    Dout.writeUTF("GiveMeThePostData_:" + Login.loginedUser.get_ID());
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


}