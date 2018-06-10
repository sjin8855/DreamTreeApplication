package com.example.user.dreamtreeapp;

import java.io.Serializable;

public class Store implements Serializable
{
    String index;
    String Sector;
    String Address;
    String PhoneNum;
    String Name;

    public String get_indexs()
    {
        return index;
    }
    public String get_Sector()
    {
        return Sector;
    }
    public String get_Address()
    {
        return Address;
    }
    public String get_PhoneNum()
    {
        return PhoneNum;
    }
    public String get_Name()
    {
        return Name;
    }

    public void set_index(String _index)
    {
        index = _index;
    }
    public void set_Sector(String _Sector)
    {
        Sector = _Sector;
    }
    public void set_Address(String _Address)
    {
        Address = _Address;
    }
    public void set_PhoneNum(String _PhoneNum)
    {
        PhoneNum = _PhoneNum;
    }
    public void set_Name(String _Name)
    {
        Name = _Name;
    }
}
