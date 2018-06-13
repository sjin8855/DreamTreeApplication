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
					System.out.println("USER Remaining Money : " + rs.getInt(7));
					System.out.println("======================");
					loginedUser.set_Name(rs.getString(1));
					loginedUser.set_Birthday(rs.getString(2));
					loginedUser.set_ID(rs.getString(3));
					loginedUser.set_PW(rs.getString(4));
					loginedUser.set_PhoneNumber(rs.getString(5));
					loginedUser.set_Email(rs.getString(6));
					loginedUser.set_RemainingMoney(rs.getInt(7));
				
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
				System.out.println("이미 존재하는 아이디입니다");
				return false;				
			}
		     
			
		        //DB에 추가
		    strSQL = "insert into db_user_INFO(name, birthday, user_id, user_pw)\r\n" + 
		                "values('" + name + "','" + birthday + "','" + id + "','" +  password+ "' );";
		    stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);
		    
	        //DB에 추가
		    strSQL = "insert into db_user_EMAIL(phone_number, email)\r\n" + 
	                	"values('" + PhoneNumber + "','" + Email + "');";
		    stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);
		    
		    strSQL = "insert into db_users(name,birthday,user_id,user_pw,phone_number, email, balance)\r\n" + 
                	"values('" +name + "','" + birthday + "','" + id + "','" + password + "','" + PhoneNumber + "','" + Email + "','" + 150000 +"');";
		    
			stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);
			System.out.println("====================");
			System.out.println("SQL : " + strSQL);
			System.out.println("--------------------");			
			
			//중복확인
			return true;
			
			
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
			return false;
		}
	}
	
	public void BudgetDataRead(String id)
	{
		try
		{
			List<BudgetInfo> budgetList = new ArrayList<BudgetInfo>();
			BudgetInfo newBudget;
			
		    strSQL = "select *\r\n" + 
		                "from db_Account_book\r\n" + 
		                "where user_id = '" + id + "'";
		    stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);		
					
			while(rs.next())
			{				
				newBudget = new BudgetInfo();
				newBudget.setUserID(rs.getString(1));
				newBudget.setCategory(rs.getString(2));
				newBudget.setSpendMoney(rs.getString(3));
				newBudget.setMemo(rs.getString(4));
				newBudget.setDate(rs.getString(5));
				
				budgetList.add(newBudget);
			}		     			
			
			ClientHandler ch;
			ch = new ClientHandler();
			ch.setBudgetList(budgetList);
			
			for(int i =0; i< budgetList.size();i++)
			{
				System.out.println(budgetList.get(i).getMemo());
			}
			
			
			strSQL = "select count(category)\r\n" + 
					"from DB_ACCOUNT_BOOK\r\n" + 
					"WHERE category = '편의점'\r\n"
					+ " AND USER_ID = '" + id + "';";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);
			
			while(rs.next())
			{				
				if(Integer.parseInt(rs.getString(1))>3)
				{
					ch.sethasBudgetProblem(true);
				}
				else
				{
					ch.sethasBudgetProblem(false);
				}
											
				System.out.println(id + "가 편의점 간 횟수: " + rs.getString(1));
			}
		}
		
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
	public boolean BudgetDataWrite(String id, String storeName, String expenditure, String balance, String memo, String spendDate)
	{
		try
		{	
			/*
			strSQL = "select u.balance, a.expenditure\r\n" + 
					"from db_user_info u,db_account_book a\r\n" + 
					"where a.user_id = u.user_id;";
					*/
			strSQL = "select balance\r\n" + 
					"from db_users\r\n" + 
					"where user_id = '" +  id + "';";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);	
			
			int remainingMoney = 0;
			while(rs.next())
			{
				System.out.println("잔액" + (rs.getInt(1) - Integer.valueOf(expenditure)));
				remainingMoney = rs.getInt(1) - Integer.valueOf(expenditure);
			}
			System.out.println("====================");
			System.out.println("SQL : " + strSQL);
			System.out.println("--------------------");	
			
			strSQL = "update DB_USERS\r\n" + 
					"set BALANCE = " + remainingMoney +"\r\n" + 
					"where user_id = '" +  id + "';";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);	
			
			System.out.println("====================");
			System.out.println("SQL : " + strSQL);
			System.out.println("--------------------");	
		
			//DB에 추가
		    strSQL = "insert into DB_ACCOUNT_BOOK(USER_ID, CATEGORY, EXPENDITURE, MEMO, SPEND_DATE)\r\n" + 
		                "values('" + id + "','" + storeName + "','" + Integer.valueOf(expenditure) + "','"  + memo + "','" + spendDate + "');";
		    
			stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);
			System.out.println("====================");
			System.out.println("SQL : " + strSQL);
			System.out.println("--------------------");			
			
			//중복확인
			return true;			
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean CommunityDataWrite(String id, String postTitle, String postContents, String date)
	{
		try
		{		     
			String Maxpostid = "";
			int postID = 0;
			
			strSQL = "select max(post_id)\r\n" + 
					"from db_community;";
	    
			stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);
			while(rs.next())
			{
				System.out.println(rs.getString(1));
				Maxpostid = String.valueOf((rs.getString(1)));
			}
			
			try
			{
				postID = Integer.parseInt(Maxpostid) + 1;
			}
			catch(NumberFormatException ex)
			{
				ex.printStackTrace();
			}
			
			String strPostID = String.valueOf(postID);
			//DB에 추가
		    strSQL = "insert into DB_COMMUNITY(USER_ID, POST_ID, POST_TITLE, POST_CONTENTS, POST_DATE)\r\n" + 
		                "values('" + id + "','" + strPostID + "','" + postTitle + "','" +  postContents + "','" + date + "');";
		    
			stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);
			System.out.println("====================");
			System.out.println("SQL : " + strSQL);
			System.out.println("--------------------");			
			
			//중복확인
			return true;			
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
			return false;
		}
	}
	
	public void CommunityDataRead()
	{
		try
		{
			List<Post> postList = new ArrayList<Post>();
			Post newPost;
			
		    strSQL = "select *\r\n" + 
		                "from DB_COMMUNITY;";
		    stmt = conn.createStatement();
			rs = stmt.executeQuery(strSQL);		
					
			while(rs.next())
			{				
				newPost = new Post();
				newPost.setTitle(rs.getString(3));
				newPost.setContent(rs.getString(4));
				newPost.setDate(rs.getString(5));				
				
				postList.add(newPost);
			}		     			
			
			ClientHandler ch;
			ch = new ClientHandler();
			ch.setPostList(postList);
			
			for(int i =0; i< postList.size();i++)
			{
				System.out.println(postList.get(i).getTitle());
			}
								
		}
		
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
	///////////////////////수정 전//////////////////////////////
	
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
				System.out.println("Store name : " + rs.getString(1));
				System.out.println("Sectors : " + rs.getString(2));
				System.out.println("Address : " + rs.getString(3));
				System.out.println("Phone Number : " + rs.getString(4));
				//System.out.println("Store ID : " + rs.getString(1));
				System.out.println("Borough name : " + rs.getString(5));


				System.out.println("======================");
				store.set_Name( rs.getString(1));
				store.set_Sector(rs.getString(2));
				store.set_Address(rs.getString(3));
				store.set_PhoneNum(rs.getString(4));
				store.set_Borough(rs.getString(5));
					
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
	
	
	//////////////////////////수정 후 /////////////////////////////
	//변경 점 : 레스토랑 디비를 정규화하여 두개로 분리함. 
	//레스토랑 이름은 섹터와 연관성 있으므로 Bcnf 로 설정하고, 그 이외의 분해는 불필요한 분해임으로 3 정규형으로 놔둠.
//	public static synchronized void getRestaurantInfoData()
//	{
//		try
//		{
//			Store store ;
//			List<Store> storeList = new ArrayList<Store>();
//			System.out.println(conn);
//			strSQL = "select * from DB_RESTAURANT_INFO";
//			stmt = conn.createStatement();
//			rs = stmt.executeQuery(strSQL);
//			System.out.println("STMT: " + stmt);
//			System.out.println("====================");
//			System.out.println("SQL : " + strSQL);
//			System.out.println("--------------------");			
//			
//			while(rs.next())
//			{
//				store = new Store();
//				System.out.println("Phone Number : " + rs.getString(1));
//				System.out.println("Store Name : " + rs.getString(2));
//				System.out.println("Borough Name : " + rs.getString(3));
//				System.out.println("Address : " + rs.getString(4));
//				//System.out.println("Address : " + rs.getString(5));
//				//System.out.println("Phone Number : " + rs.getString(6));
//				System.out.println("======================");
//				store.set_PhoneNum( rs.getString(1));
//				store.set_Name(rs.getString(2));
//				store.set_Borough(rs.getString(3));
//				store.set_Address(rs.getString(4));
//					
//				storeList.add(store);				
//			}		
//			
//			for(int i = 0; i<storeList.size();i++)
//        	{
//        		System.out.println("name: " + storeList.get(i).get_Name());   
//        	}
//			ClientHandler ch;
//			ch = new ClientHandler();
//			ch.setStoreList(storeList);
//			System.out.println("--------------------");
//		}
//		catch(Exception ex) 
//		{
//			ex.printStackTrace();
//		}
//	}
//	
//	public static synchronized void getRestaurantSectorData()
//	{
//		try
//		{
//			Store store ;
//			List<Store> storeList = new ArrayList<Store>();
//			System.out.println(conn);
//			strSQL = "select * from DB_RESTAURANT_sectors";
//			stmt = conn.createStatement();
//			rs = stmt.executeQuery(strSQL);
//			System.out.println("STMT: " + stmt);
//			System.out.println("====================");
//			System.out.println("SQL : " + strSQL);
//			System.out.println("--------------------");			
//			
//			while(rs.next())
//			{
//				store = new Store();
//				//System.out.println("Store ID : " + rs.getString(1));
//				//System.out.println("Borough name : " + rs.getString(2));
//				System.out.println("Store name : " + rs.getString(1));
//				System.out.println("Sectors : " + rs.getString(2));
//				//System.out.println("Address : " + rs.getString(5));
//				//System.out.println("Phone Number : " + rs.getString(6));
//				System.out.println("======================");
//				store.set_Name( rs.getString(1));
//				store.set_Sector(rs.getString(2));
//				//store.set_Address(rs.getString(3));
//				//store.set_PhoneNum(rs.getString(4));
//					
//				storeList.add(store);				
//			}
//			
//			for(int i = 0; i<storeList.size();i++)
//        	{
//        		System.out.println("name: " + storeList.get(i).get_Name());   
//        	}
//			ClientHandler ch;
//			ch = new ClientHandler();
//			ch.setStoreList(storeList);
//			System.out.println("--------------------");
//		}
//		catch(Exception ex) 
//		{
//			ex.printStackTrace();
//		}
//	}
	
}
