package com.example.user.dreamtreeapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class JDBC extends Thread
{
	static Connection conn = null;
	 static Statement stmt = null;
	 static ResultSet rs = null;

	 static String DB_DRV = "com.tmax.tibero.jdbc.TbDriver";
	 static String DB_IP = "localhost";
	 static String DB_PORT = "8629";
	 static String DB_SID = "tibero";
	 static String DB_ID = "HR";
	 static String DB_PWD = "tibero";
	 static String DB_URL = "jdbc:tibero:thin:@" + DB_IP + ":" + DB_PORT + ":" + DB_SID;
	 

	 
	 static String strSQL;

	public JDBC() {
		
		// TODO Auto-generated constructor stub
	}
	public void connect()
	{
		try
		{
			System.out.println("=====================");
			System.out.println("DB_DRV : " + DB_DRV);
			System.out.println("DB_URL : " + DB_URL);
			System.out.println("DB_ID : " + DB_ID);
			System.out.println("DB_PWD : " + DB_PWD);
			Class.forName(DB_DRV);
			conn = DriverManager.getConnection(DB_URL, DB_ID, DB_PWD);
			System.out.println(conn);
			System.out.println("Tibero Connect Success");
			System.out.println("=====================");
			System.out.println("");
		}
		
		catch(Exception xt)
		{
			xt.printStackTrace();
		}
	}
	
	
	public User CheckUserInfo(String id, String pw)
	{
		try
		{
			User loginedUser = new User();
			System.out.println(conn);
			strSQL = "select * from DB_USERS WHERE USER_ID = '" +id + "'AND USER_PW = '" + pw + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);
			System.out.println("STMT: " + stmt);
			System.out.println("====================");
			System.out.println("SQL : " + strSQL);
			System.out.println("--------------------");			
			
			while(rs.next())
			{
				if(rs.getString(3).equals(id) && rs.getString(4).equals(pw))
				{
					System.out.println("USER Name : " + rs.getString(1));
					System.out.println("USER BirthDay : " + rs.getString(2));
					System.out.println("USER ID : " + rs.getString(3));
					System.out.println("USER PW : " + rs.getString(4));
					System.out.println("USER PhoneNumber : " + rs.getString(5));
					System.out.println("USER Email : " + rs.getString(6));
					System.out.println("======================");
					loginedUser.set_Name(rs.getString(1));
					loginedUser.set_Birthday(rs.getString(2));
					loginedUser.set_ID(rs.getString(3));
					loginedUser.set_PW(rs.getString(4));
					loginedUser.set_PhoneNumber(rs.getString(5));
					loginedUser.set_Email(rs.getString(6));
				
					return loginedUser;
				}
				
			}
			System.out.println("--------------------");
			return null;
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public boolean UserDataWrite(String id, String password, String name, String birthday, String PhoneNumber, String Email)
	{
		try
		{
		    strSQL = "select USER_ID\r\n" + 
		                "from db_users\r\n" + 
		                "where user_id = '" + id + "'";
		    stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);
					
			while(rs.next())
			{				
				System.out.println(rs.getString(1));
				System.out.println("�̹� �����ϴ� ���̵��Դϴ�");
				return false;				
			}
		     
		        //DB�� �߰�
		    strSQL = "insert into db_users(name, birthday, user_id, user_pw, phone_number, email)\r\n" + 
		                "values('" + name + "','" + birthday + "','" + id + "','" +  password + "','" + PhoneNumber + "','" + Email + "');";
		    
			stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);
			System.out.println("====================");
			System.out.println("SQL : " + strSQL);
			System.out.println("--------------------");			
			
			//�ߺ�Ȯ��
			return true;
			
			
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
			return false;
		}
	}
	
	public static synchronized void getRestaurantData()
	{
		try
		{
			Store store ;
			List<Store> storeList = new ArrayList<Store>();
			System.out.println(conn);
			strSQL = "select * from DB_RESTAURANT";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);
			System.out.println("STMT: " + stmt);
			System.out.println("====================");
			System.out.println("SQL : " + strSQL);
			System.out.println("--------------------");			
			
			while(rs.next())
			{
				store = new Store();
				System.out.println("Store ID : " + rs.getString(1));
				System.out.println("Borough name : " + rs.getString(2));
				System.out.println("Store name : " + rs.getString(3));
				System.out.println("Sectors : " + rs.getString(4));
				//System.out.println("Address : " + rs.getString(5));
				//System.out.println("Phone Number : " + rs.getString(6));
				System.out.println("======================");
				store.set_Name( rs.getString(1));
				store.set_Sector(rs.getString(2));
				store.set_Address(rs.getString(3));
				store.set_PhoneNum(rs.getString(4));
					
				storeList.add(store);				
			}
			
			for(int i = 0; i<storeList.size();i++)
        	{
        		System.out.println("name: " + storeList.get(i).get_Name());   
        	}
			ClientHandler ch;
			ch = new ClientHandler();
			ch.setStoreList(storeList);
			System.out.println("--------------------");
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
}
