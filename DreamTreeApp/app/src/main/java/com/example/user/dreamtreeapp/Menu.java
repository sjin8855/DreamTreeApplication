package com.example.user.dreamtreeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
        Intent intent = new Intent(getApplicationContext(),Budget.class);
        startActivity(intent);
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
}
