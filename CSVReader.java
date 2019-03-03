//Done by Johnny Lai for job interview coding test
//Implement with native Java Library

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import javax.swing.JFrame; 
import javax.swing.JScrollPane; 
import javax.swing.JTable; 
import javax.swing.WindowConstants;
 
public class CSVReader {

    public static void main(String[] args) {

        String csvFile = "test_data.csv";
        String line = "";
		
		//Use to cater the comma and double quote issue
        String cvsSplitBy = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
		int lineReading = 1;
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		String userInput;
		
		try	{
			userInput = args[0]; }
		catch (ArrayIndexOutOfBoundsException e)	{
			userInput = "id";
		}	
		
		String[] columnNames = new String[7];
		
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
				
				if (lineReading > 1)	{
					String[] country = line.split(cvsSplitBy);

					int id;
					String firstName;
					String lastName;
					String email;
					String gender;
					String ip;
					double balance;	

					id = Integer.parseInt(country[0]);
					firstName = country[1];
					lastName = country[2];
					email = country[3];

					//cater different length records, some with all 7 fields, some only got 4 fields
					if (country.length <= 4)	{	
						gender = "";	}	
					else	{
						gender = country[4];	}
					if (country.length <= 5)	{	
						ip = "";	}	
					else	{
						ip = country[5];	}
					if (country.length <= 6 )	{
						balance = 0;	}
					else	{	
						balance = Double.parseDouble(country[6].replaceAll("([\"|,])", ""));	}
					
					//Stores in object, easier to sort by comparator
					UserInfo userInfo = new UserInfo(id, firstName, lastName, email, gender, ip, balance);
					userInfoList.add(userInfo);
				}	else{
					//save column name from CSV
					columnNames = line.split(cvsSplitBy);	
				}	
				lineReading++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

		//cater the requirement of user-specific sorting field
	        //If the input not matching the field name, it would also sort by ID
		if (userInput.equals(columnNames[0]))	{
			Collections.sort(userInfoList, UserInfo.idComparator);
		}	else if (userInput.equals(columnNames[1]))	{
			Collections.sort(userInfoList, UserInfo.firstNameComparator);
		}	else if (userInput.equals(columnNames[2]))	{
			Collections.sort(userInfoList, UserInfo.lastNameComparator);
		}	else if (userInput.equals(columnNames[3]))	{
			Collections.sort(userInfoList, UserInfo.emailComparator);
		}	else if (userInput.equals(columnNames[4]))	{
			Collections.sort(userInfoList, UserInfo.genderComparator);
		}	else if (userInput.equals(columnNames[5]))	{
			Collections.sort(userInfoList, UserInfo.ipComparator);
		}	else if (userInput.equals(columnNames[6]))	{
			Collections.sort(userInfoList, UserInfo.balanceComparator);
		}	else	{
			Collections.sort(userInfoList, UserInfo.idComparator);	}

		//As we have 1000 records, so better to display it in GUI rather than command console
        JFrame jframe = new JFrame(); 
 
        jframe.setTitle(csvFile); 
  
		String [][] data = new String[userInfoList.size()][7];
		
		for (int i=0; i<userInfoList.size(); i++)	{
			data[i][0] = ((UserInfo)userInfoList.get(i)).getId()+"";
			data[i][1] = ((UserInfo)userInfoList.get(i)).getFirstName()+"";
			data[i][2] = ((UserInfo)userInfoList.get(i)).getLastName()+"";
			data[i][3] = ((UserInfo)userInfoList.get(i)).getEmail()+"";
			data[i][4] = ((UserInfo)userInfoList.get(i)).getGender()+"";
			data[i][5] = ((UserInfo)userInfoList.get(i)).getIp()+"";
			data[i][6] = ((UserInfo)userInfoList.get(i)).getBalance()+"";			
		}	
		
        JTable jTable = new JTable(data, columnNames); 
        jTable.setBounds(50, 50, 200, 300); 
  
        JScrollPane jscrollPane = new JScrollPane(jTable); 
        jframe.add(jscrollPane); 
        jframe.setSize(700, 700); 

        jframe.setVisible(true); 
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
class UserInfo	{
		int id;
		String firstName;
		String lastName;
		String email;
		String gender;
		String ip;
		double balance;
		
		public UserInfo(int id, String firstName, String lastName, String email, String gender, String ip, double balance)	{
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.gender = gender;
			this.ip = ip;
			this.balance = balance;
		}	

		public int getId() {
		return id;
		}


		public void setId(int id) {
		this.id = id;
		}


		public String getFirstName() {
		return firstName;
		}


		public void setFirstName(String firstName) {
		this.firstName = firstName;
		}


		public String getLastName() {
		return lastName;
		}


		public void setLastName(String lastName) {
		this.lastName = lastName;
		}


		public String getEmail() {
		return email;
		}


		public void setEmail(String email) {
		this.email = email;
		}


		public String getGender() {
		return gender;
		}


		public void setGender(String gender) {
		this.gender = gender;
		}


		public String getIp() {
		return ip;
		}


		public void setIp(String ip) {
		this.ip = ip;
		}


		public double getBalance() {
		return balance;
		}


		public void setBalance(double balance) {
		this.balance = balance;
		}	

		//Comparators to facilitate the sorting
		public static Comparator<UserInfo> idComparator = new Comparator<UserInfo>() {         
        
			public int compare(UserInfo uiA, UserInfo uiB) {             
			  return uiA.getId() - uiB.getId();         
			}     
		}; 		

		public static Comparator<UserInfo> firstNameComparator = new Comparator<UserInfo>() {         
        
			public int compare(UserInfo uiA, UserInfo uiB) {             
			  return (int) (uiA.getFirstName().compareTo(uiB.getFirstName()));         
			}     
		}; 		

		public static Comparator<UserInfo> lastNameComparator = new Comparator<UserInfo>() {         
        
			public int compare(UserInfo uiA, UserInfo uiB) {             
			  return (int) (uiA.getLastName().compareTo(uiB.getLastName()));         
			}     
		}; 
		

		public static Comparator<UserInfo> emailComparator = new Comparator<UserInfo>() {         
        
			public int compare(UserInfo uiA, UserInfo uiB) {             
			  return (int) (uiA.getEmail().compareTo(uiB.getEmail()));         
			}     
		}; 

		public static Comparator<UserInfo> genderComparator = new Comparator<UserInfo>() {         
        
			public int compare(UserInfo uiA, UserInfo uiB) {             
			  return (int) (uiA.getGender().compareTo(uiB.getGender()));         
			}     
		}; 		

		public static Comparator<UserInfo> ipComparator = new Comparator<UserInfo>() {         
        
			public int compare(UserInfo uiA, UserInfo uiB) {             
			  return (int) (uiA.getIp().compareTo(uiB.getIp()));         
			}     
		}; 

		public static Comparator<UserInfo> balanceComparator = new Comparator<UserInfo>() {         
        
			public int compare(UserInfo uiA, UserInfo uiB) {             
			  return Double.compare(uiA.getBalance(), uiB.getBalance());         
			}     
		}; 
		
}	
