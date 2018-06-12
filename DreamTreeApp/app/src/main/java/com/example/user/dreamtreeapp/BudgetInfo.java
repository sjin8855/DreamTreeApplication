package com.example.user.dreamtreeapp;

import java.io.Serializable;

public class BudgetInfo implements Serializable
{
    String userID;
    String date;
    String spendMoney;
    String category;
    String memo;
    String balance;

    public String getUserID()
    {
        return userID;
    }
    public String getDate()
    {
        return date;
    }
    public String getSpendMoney()
    {
        return spendMoney;
    }
    public String getCategory()
    {
        return category;
    }
    public String getMemo()
    {
        return memo;
    }
    public String getBalance()
    {
        return balance;
    }

    public void setUserID(String _userID)
    {
        userID = _userID;
    }
    public void setDate(String _date)
    {
        date = _date;
    }
    public void setSpendMoney(String _spendMoney)
    {
        spendMoney = _spendMoney;
    }
    public void setCategory(String _category)
    {
        category = _category;
    }
    public void setMemo(String _memo)
    {
        memo = _memo;
    }
    public void setBalance(String _balance)
    {
        balance = _balance;
    }
}