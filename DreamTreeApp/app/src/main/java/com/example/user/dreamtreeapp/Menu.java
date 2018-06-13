package com.example.user.dreamtreeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class Menu extends AppCompatActivity {

    Button LoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        LoginButton = (Button) findViewById(R.id.LoginButton);
        if(Login.isLogined)
        {
            LoginButton.setText(Login.loginedUser.get_Name());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        getMenuInflater().inflate(R.menu.actionbar_actions,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.actionLogin:
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onMapButtonClicked(View v)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    public void onBudgetButtonClicked(View v)
    {
        if(Login.isLogined)
        {
            Intent intent = new Intent(getApplicationContext(), Budget.class);
            startActivity(intent);
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),"로그인부터 해주세요",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void onSettingButtonClicked(View v)
    {
        StoreManager sm = new StoreManager();
        List<Store> storeList =  sm.getStoreinfo();
        for(int i = 0; i<storeList.size();i++)
        {
            System.out.println("na: " + storeList.get(i).get_Name());
        }
        Intent intent = new Intent(getApplicationContext(),Setting.class);
        startActivity(intent);
    }

    public void onLoginButtonClicked(View v)
    {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    public void onButtonCommunityClicked(View v)
    {
        if(Login.isLogined)
        {
            Intent intent = new Intent(getApplicationContext(), PostList.class);
            startActivity(intent);
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),"로그인부터 해주세요",Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
