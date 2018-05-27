package com.example.user.dreamtreeapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class StoreManager
{
    public static List<Store> storeList;

    public void setStoreList(List<Store> store)
    {
        storeList = store;
    }
    public List<Store> getStoreinfo()
    {
        return storeList;
    }


}
