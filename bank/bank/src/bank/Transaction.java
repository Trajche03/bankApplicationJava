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
import java.util.ArrayList;

/**
 *
 * @author Trajche
 */
public  class Transaction {
    private int id;
    private  float amount;
    private int originating_account_id;
    private int resulting_account_id;
    private String transaction_reason;
    private float transaction_fee;
    
    //overiding default Transaction contructor to create Transaction from console input
    public Transaction(float am, int oaid, int raid, String tr, float tf) {
        this.amount = am;
        this.originating_account_id = oaid;
        this.resulting_account_id = raid;
        this.transaction_reason = tr;
        this.transaction_fee = tf;
        this.saveInDatabase(am, oaid, raid,tr, tf);
    }
    
    // function that save Transaction in Database
    public void saveInDatabase(float am, int oaid, int raid, String tr, float tf) {
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
            pst = myConn.prepareStatement("Insert into transaction(amount, originating_account_id, resulting_account_id, transaction_reason, transaction_fee) values(?,?,?, ?, ?)");
        
            pst.setFloat(1, am);
            pst.setInt(2, oaid);
            pst.setInt(3,  raid);
            pst.setString(4, tr);
            pst.setFloat(5, tf);
            int action = pst.executeUpdate();
          
            if( action > 0) {
                System.out.println("Transaction was successfully added in database");
            }else {
                System.out.println("An error occured, try again");
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } 
    }

    public double getAmount(){
        return this.amount;
    }
    public int getOriginating_account_id(){
        return this.originating_account_id;
    }
    
    public  int getResulting_account_id(){
        return this.resulting_account_id;
    }
    
    public String getTransaction_reason(){
        return this.transaction_reason;
    }
    
    //function that return list of transaction for given account
    public ArrayList<Transaction> ListTransaction(Account a) {
        ArrayList<Transaction> listTransaction = new ArrayList<Transaction>();
        if(this.originating_account_id == a.getAccount_id() ) {
            listTransaction.add(this);
        }
        if(this.resulting_account_id == a.getAccount_id()) {
            listTransaction.add(this);
        }
        
        return listTransaction;
    }
    
    
    public static void main(String[] args) {
        
        
        
    }
    
            

}



