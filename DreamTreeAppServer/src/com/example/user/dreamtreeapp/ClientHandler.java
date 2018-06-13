package com.example.user.dreamtreeapp;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientHandler extends Thread
{
	DataInputStream dis;
	DataOutputStream dos;
	ObjectOutputStream oos;
	Socket s;
	
	Boolean isLogin = false;
	String CheckLogin = "";
	
	static List<User> list;
	static User checked_user;
	static HashMap<String, String> userStatusList;
	
	static String data;
	static String[] statedata;
	
	static String m_id;
	
    static JDBC myDB = new JDBC();
    
	static List<Store> storeList;
	static List<BudgetInfo> budgetList;
	static List<Post> postList;
	static boolean hasBudgetProblem = false;
    
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
	{    	
    	System.out.println("ClientHandler created");
		this.s = s;
		this.dis = dis;
		this.dos = dos;
		
		ServerReceiver sr = new ServerReceiver(s);
		sr.start();
	}
	
	public ClientHandler()
	{
		
		System.out.println("new List created");
		checked_user = new User();
	}
	
	 class ServerReceiver extends Thread 
	 {	 
		 Socket socket;
	        public ServerReceiver(Socket socket) {
	        	System.out.println("new ServerReceived created");
	            this.socket = socket;
	            try 
	            {          	
	            	myDB.connect();
	            	//FileIO fileThread = new FileIO();
	            	//fileThread.ThreadStart();
	                dis = new DataInputStream(socket.getInputStream());
	                dos = new DataOutputStream(socket.getOutputStream());              
	            } 
	            catch (IOException ie) 
	            {            	
	            }
	        }
	 
	        public void run()
	        {
	            String name = "";
	            
	            try 
	            {
	            	data = dis.readUTF();
	            	System.out.println("data   " + data);
	            	if(data.contains("LoginID_"))
	            	{
	            		getUserInfoFromClient();
	            	}
	            	else if(data.contains("SignUpD_"))
	            	{
	            		System.out.println("get Sign up Data");
	            		SignUp();
	            	}
	            	else if(data.contains("Received well"))
	            	{
	            		System.out.println("Received well from client");
	            	}
	            	else if(data.contains("GetStoreInfo!!@@"))
	            	{
	            		System.out.println("Get Store Info Start!");
	            		getStoreInfo();
	            		//sendUserState();
	            	}
	            	else if(data.contains("GiveMeTheBudgetData_"))
	            	{
	            		String[] splited = data.split(":");
	            		m_id = splited[1];
	            		getBudgetInfo();
	            	}
	            	else if(data.contains("writeBudgetInfo_"))
	            	{
	            		String[] splited = data.split(":");
	            		getBudgetInfoFromClient();
	            	}
	            	else if(data.contains("writePostInfo_"))
	            	{
	            		String[] splited = data.split(":");
	            		getCommunityInfoFromClient();
	            	}	            	
	            	else if(data.contains("GiveMeThePostData_"))
	            	{
	            		String[] splited = data.split(":");
	            		m_id = splited[1];
	            		System.out.println("Ssss");
	            		getCommunityInfo();
	            	}
	            }
	            catch (IOException ie) 
	            {
	            	
	            } 
	            finally {
	            }
	        }
	        public void getUserInfoFromClient() throws IOException
	        {
	        	System.out.println("getUserInfoFromClient Start from handler");
	        	//System.out.println("Current User: " + checked_user.get_Status());	        
	        	
	        	String ID = "";
	        	String PW = "";
				System.out.println("data: " + data);
	        	if(data.contains("LoginID_"))
				{
					String[] splited = data.split(":");
					ID = splited[0].replace("LoginID_", "");
					System.out.println("ID: " + ID);
					PW = splited[1].replace("LoginPW_", "");
					System.out.println("PW: " + PW);	
					
					checked_user = myDB.CheckUserInfo(ID, PW);
					if(checked_user != null)
					{
						System.out.println("Current User ID: " + checked_user.get_ID());
						System.out.println("Current User PW: " + checked_user.get_PW());
						dos.writeUTF("LoginSuccessFull@!@!" + ":" 
						+ "LoginedUserID_" + checked_user.get_ID() 
						+ ":" + "LoginedUserPW_" + checked_user.get_PW() 
						+ ":" + "LoginedUserName_" + checked_user.get_Name() 
						+  ":" + "LoginedUserBirthday_" + checked_user.get_Birthday() 
						+ ":" + "LoginedUserPhoneNumber_" + checked_user.get_PhoneNumber() 
						+ ":" + "LoginedUserEmail_" + checked_user.get_Email()
						+ ":" + "LoginedUserBalance_" + checked_user.get_RemainingMoney());
						System.out.println("logined");
						dos.flush();
					}
					else if(checked_user == null)
					{
						socket.close();
						return;
					}
				}			 			
	        }
	        
	        public void getBudgetInfoFromClient() throws IOException
	        {
	        	System.out.println("getBudgetInfoFromClient Start from handler");
	        	//System.out.println("Current User: " + checked_user.get_Status());	        
	        	//BudgetUserID, BudgetCategory, BudgetExpenditure, BudgetMemo, BudgetSpendDate
	        	
	        	String BudgetUserID = "";
	        	String BudgetCategory = "";
	        	String BudgetExpenditure = "";
	        	String BudgetMemo = "";
	        	String BudgetSpendDate = "";
				System.out.println("data: " + data);
				
	        	if(data.contains("writeBudgetInfo_"))
				{
					String[] splited = data.split(":");
					BudgetUserID = splited[2];
					System.out.println("BudgetUserID: " + BudgetUserID);
					BudgetCategory = splited[4];
					System.out.println("BudgetCategory: " + BudgetCategory);	
					BudgetExpenditure = splited[6];
					System.out.println("BudgetExpenditure: " + BudgetExpenditure);
					BudgetMemo = splited[8];
					System.out.println("BudgetMemo: " + BudgetMemo);
					BudgetSpendDate = splited[10];
					System.out.println("BudgetSpendDate: " + BudgetSpendDate);
				
					myDB.BudgetDataWrite(BudgetUserID, BudgetCategory, BudgetExpenditure, "0", BudgetMemo, BudgetSpendDate);
					dos.writeUTF("BudgetListUpdated__");
					System.out.println("BudgetListUpdated");
					dos.flush();
					
					return;
				}		
	        }
	        public void getCommunityInfoFromClient() throws IOException
		    {
		        	System.out.println("getCommunityInfoFromClient Start from handler");
		        	//System.out.println("Current User: " + checked_user.get_Status());	        
		        	//BudgetUserID, BudgetCategory, BudgetExpenditure, BudgetMemo, BudgetSpendDate
		        	
		        	String CommunityUserID = "";
		        	String CommunityTitle = "";
		        	String CommunityContent = "";
		        	String CommunityDate = "";
					System.out.println("data: " + data);
					
		        	if(data.contains("writePostInfo_"))
					{
						String[] splited = data.split(":");
						CommunityUserID = splited[2];
						System.out.println("CommunityUserID: " + CommunityUserID);
						CommunityTitle = splited[4];
						System.out.println("CommunityTitle: " + CommunityTitle);	
						CommunityContent = splited[6];
						System.out.println("CommunityContent: " + CommunityContent);
						CommunityDate = splited[8];
						System.out.println("CommunityDate: " + CommunityDate);
					
						myDB.CommunityDataWrite(CommunityUserID, CommunityTitle, CommunityContent,CommunityDate);
						dos.writeUTF("CommunityListUpdated__");
						System.out.println("CommunityListUpdated__");
						dos.flush();
						
						return;
					}	
	        }
	        
	        public void SignUp() throws IOException
	        {
	        	String SignUpID = "";
	        	String SignUpPW = "";
	        	String SignUpName = "";
	        	String SignUpBirthday = "";
	        	String SignUpPhoneNumber = "";
	        	String SignUpEmail = "";
	        	if(data.contains("SignUpD_"))
				{
	        		String[] splited = data.split(":");
	        		SignUpID = splited[0].replace("SignUpD_", "");
	        		System.out.println("SignUpID: " + SignUpID);
	        		SignUpPW = splited[1].replace("SignUpPW_", "");
					System.out.println("SignUpPW: " + SignUpPW);
					SignUpName = splited[2].replace("SignUpName_", "");
					System.out.println("SignUpName: " + SignUpName);
					SignUpBirthday = splited[3].replace("SignUpBirthday_", "");
					System.out.println("SignUpBirthday: " + SignUpBirthday);
					SignUpPhoneNumber = splited[4].replace("SignUpPhoneNumber_", "");
					System.out.println("SignUpPhoneNumber: " + SignUpPhoneNumber);
					SignUpEmail = splited[5].replace("SignUpEmail_", "");
					System.out.println("SignUpEmail: " + SignUpEmail);					
					
					if(myDB.UserDataWrite(SignUpID, SignUpPW, SignUpName,SignUpBirthday,SignUpPhoneNumber,SignUpEmail))
					{
						System.out.println("회원가입 성공");
						dos.writeUTF("SignUpSuccessfull@!#!@#");
						dos.flush();
					}
					else
					{
						dos.writeUTF("ID Already Exists!@#!@#");
						dos.flush();
					}
				}
	        }
	        public void getStoreInfo()
	        {
	        	System.out.println("Get Store Info function Start");
	        	
	        	myDB.getRestaurantData();
	        	//myDB.getRestaurantInfoData();	//수정한 부분. 
	        	//myDB.getRestaurantSectorData(); //수정한 부분
	        	
	        	System.out.println(storeList.size());
	        	
	        	for(int i = 0; i<storeList.size();i++)
	        	{
	        	//	System.out.println("name: " + storeList.get(i).get_Name() + "Borough: " + storeList.get(i).get_BoroughName());   
	        	}
	        	
	        	try 
				{
	                oos = new ObjectOutputStream(socket.getOutputStream());	 
					oos.writeObject(storeList);
					oos.flush();
					oos.close();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	        public void getBudgetInfo()
	        {
	        	System.out.println("Get Budget Info function Start");
	        	
	        	myDB.BudgetDataRead(m_id);
	        		               	
	        	try 
				{
	                oos = new ObjectOutputStream(socket.getOutputStream());	 
					oos.writeObject(budgetList);
					oos.flush();
					
					if(hasBudgetProblem)
					{
						oos.writeUTF("ThisChildIsInDanger!@!@");
						System.out.println("ThisChildIsInDanger!@!@");
						oos.flush();
					}
					for(int i=0;i<budgetList.size();i++)
	                {
	                    System.out.println(budgetList.get(i).getSpendMoney() + " :::::: " + budgetList.size());
	                }
					
					oos.close();
					
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	        public void getCommunityInfo()
	        {
	        	System.out.println("Get Budget Info function Start");
	        	
	        	myDB.CommunityDataRead();
	        		               	
	        	try 
				{
	                oos = new ObjectOutputStream(socket.getOutputStream());	 
					oos.writeObject(postList);
					oos.flush();
					
					/*
					if(hasBudgetProblem)
					{
						oos.writeUTF("ThisChildIsInDanger!@!@");
						System.out.println("ThisChildIsInDanger!@!@");
						oos.flush();
					}
					*/
					oos.close();
					
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	        
	        /*
	        public void setUserState() throws IOException
	        {
	        	System.out.println("Writing User State in Server");
	        	JDBC myDB = new JDBC();
	        
	        	String ID = "";
	        	String[] splited = statedata[0].split(":");
	        	if(data.contains("StatusIs_OFFLINE"))
            	{
	        		System.out.println("data contains Status os offline");
	        		myDB.UserStateFileWrite(splited[0], "OFFLINE");
	        		myDB.updateUserStateFile(splited[0],"OFFLINE");
            	}
	        	else if(data.contains("StatusIs_BUSY"))
            	{
	        		System.out.println("data contains Status os busy");
	        		myDB.UserStateFileWrite(splited[0], "BUSY");
	        		myDB.updateUserStateFile(splited[0],"BUSY");
            	}
	        	else if(data.contains("StatusIs_ONLINE"))
            	{
	        		System.out.println("data contains Status os online");
	        		myDB.UserStateFileWrite(splited[0], "ONLINE");
	        		myDB.updateUserStateFile(splited[0],"ONLINE");
            	}
	        }
	        public void sendUserState() throws IOException
	        {	        	
	        	System.out.println("Sending User State Date to Client");
	        	String PartnerID = statedata[1].replace("CheckUsersState_:", "");
	                	        	
	        	JDBC myDB = new JDBC();
	        	myDB.ReadUserState();
	        	
	        	Set set = userStatusList.entrySet();
				Iterator iterator = set.iterator();
				while(iterator.hasNext())
				{
					Map.Entry mentry = (Map.Entry)iterator.next();
					if(mentry.getKey().equals(PartnerID))
					{
						dos.writeUTF("State_" + mentry.getValue().toString());
						System.out.println("State_" + mentry.getValue().toString());
						dos.flush();
						return;
					}
				}
				dos.writeUTF("State_OFFLINE");
				System.out.println("State_OFFLINE");
				dos.flush();
				
	        }
	        */
	    }
	    
	    public void setUserList(List<User> userList)
		{
			list = userList;
		}
	    public void setStoreList(List<Store> _storeList)
		{
			storeList = _storeList;
		}
	    public void setUserStateList(HashMap<String, String> userState)
		{
	    	userStatusList = userState;
		}	 
	    public void setBudgetList(List<BudgetInfo> _budgetList)
		{
	    	budgetList = _budgetList;
		}	 
	    public void sethasBudgetProblem(boolean _hasBudgetProblem)
	    {
	    	hasBudgetProblem = _hasBudgetProblem;
	    }
	    public void setPostList(List<Post> _postList)
	    {
	    	postList = _postList;
	    }
}
