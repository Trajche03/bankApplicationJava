package bank;

import java.sql.*;
import java.util.*;



/**
 *
 * @author Trajche
 * IN this system flat fee are used when users withdraw money from their account
 * In this system percent fee are used when users transfer amount from one to another account
 */
public class Bank {
    
    public String bank_name;
    public float total_transaction_fee_amount;
    public float total_transfer_amount;
    //transaction_flat must be int in $
    public int transaction_flat_fee_amount;
    // transaction procent must be int in procent%
    public int transaction_procent_fee_value;
    public int account_id;
   
   ArrayList<Integer> listAccount = new ArrayList<Integer>();
    
   //overiding of default contructor(this contructor accept values from console enter
    public Bank(String b_name,ArrayList<Integer> list,float tta, float ttfa,int tffa, int tpfv) {
        this.bank_name = b_name;
        this.listAccount = list;
        this.total_transfer_amount = tta;
        this.total_transaction_fee_amount = ttfa;
        this.transaction_flat_fee_amount = tffa;
        this.transaction_procent_fee_value = tpfv;
        //this is for saving in database every bank that is created from console
        //please save in database bank table bank, for every account id create new row because bank_name and account id are primary key in the table
        for(Integer x: list) {
            this.saveInDatabase(b_name, x, tta,ttfa, tffa, tpfv);
        }  
    }
    
    //function that return list of all accounts for given Bank
    public ArrayList<Integer> getAccounts(String bname) {
        ArrayList<Integer> listAccount = new ArrayList<Integer>();
        if(this.bank_name == bname) {
            listAccount.add(this.account_id);   
        }
        return this.listAccount;
       
    }

    // function that save created bank in database 
    public void saveInDatabase(String bn,int acc_id,float ttfa, float tta, int tffa, int tpfv ) {
        
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
            pst = myConn.prepareStatement("Insert into bank ( bank_name,total_transfer_amount,"
                   + "total_transaction_fee_amount,transaction_flat_fee_amount,transaction_procent_fee_value, account_id) values(?,?,?,?,?,?)");
        
            pst.setString (1, bn);
              
            pst.setFloat(2,  ttfa);
            pst.setFloat(3,  tta);
            pst.setFloat(4,  tffa);
            pst.setFloat(5, tpfv);
            pst.setInt(6, acc_id);
            int action = pst.executeUpdate();
           
            if(action > 0) {
                System.out.println("Data was successfully insert in database");
            }else {
                System.out.println("Something went wrong");
            }
          
           

        } catch (SQLException e) {
           
            e.printStackTrace();
            
        }
       
    }
    
    public static void main(String[] args) {

    
    }

}

    



  