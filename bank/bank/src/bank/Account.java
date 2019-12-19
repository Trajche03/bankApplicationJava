/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

/**
 *
 * @author Trajche
 */
public  class Account {
     
    private int account_id;
    public String name;
    private float balance;
    
    //construct for new object of account with no input parameters
    public Account() {
        Random random = new Random();
        int x = random.nextInt(900) + 100;
        this.account_id = x;
        this.name = "Account_name_"+account_id;
        this.balance = Float.parseFloat("0");
    }
    //ovveriding of the construct this has input parameters to create Account from console input
    public Account(int acc_id, String name, float balance) {
        this.account_id = acc_id;
        this.name = name;
        this.balance = balance;
        this.saveInDatabase(acc_id, name, balance);
    }
    //function that save created Account in database bank table account
    public void saveInDatabase(int acc_id, String name, float balance) {
        Connection myConn = null;
        PreparedStatement pst = null;
        ResultSet myRs = null;
      
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost:3306/bank?verifyServerCertificate=false&useSSL=true";


       String USER = "root";
       String PASS = "root"; 


        try {
        
            myConn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 3. Execute SQL query
            pst = myConn.prepareStatement("Insert into account(account_id, user_name, account_balance) values(?,?,?)");
        
            pst.setInt(1, acc_id);
            pst.setString(2, name);
            pst.setFloat(3,  balance);
            int action = pst.executeUpdate();
          
          if( action > 0) {
            System.out.println("Account was successfully created");
            this.showAccount();
          }else {
            System.out.println("An error occured, try again");
          }
          
           

        } catch (Exception exc) {
            exc.printStackTrace();
        } 
    }
    
  
    public void setAccount_id(int acc_id) {
        this.account_id = acc_id;
    }
    public void setBalance(float bal) {
        this.balance = bal;
    }
    
    public int getAccount_id(){
        return account_id;
    }
    public float getAccountBalance (Account a){
        return a.balance;
    }
    //show created account
    public void showAccount() { 
	System.out.println(account_id+","+name+","+balance);
    }
    
    
    //function that withdraw money from the account
    public void withdraw(Account a, float sum) {
        if(this.balance < sum) {
            System.out.println("There is not enought money on this account");
                    
        } else {
            this.balance = this.balance-sum;
            System.out.println("Your new balance is " + this.balance);
            
        }
        
    }
    
    //function that put money on the account
    public void deposit(int acc_id, float sum){
        this.balance+=sum;
        System.out.println("Your new balance is " + this.balance);
        
    }
     

}