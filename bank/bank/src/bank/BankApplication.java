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
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Trajche
 */
public class BankApplication {

    //function that return full account for specific account_id, select account id from database and return the account
    public static boolean getAccount(int acc_id) {
        Connection myConn = null;
        PreparedStatement pst = null;
        ResultSet myRs = null;

        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/bank?verifyServerCertificate=false&useSSL=true";

        String USER = "root";
        String PASS = "root";

        try {

            myConn = DriverManager.getConnection(DB_URL, USER, PASS);

            pst = myConn.prepareStatement("Select * from account where account_id = ?");
            pst.setInt(1, acc_id);

            ResultSet rs = pst.executeQuery();
            boolean val = rs.next();

            if (val) {
                return true;
            } else {
                return false;
            }

        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }

    }

    //function that put money on the account return 1 for success transfer
    public static int deposit(int acc_id, float sum) {
        int deposit = 0;

        Connection myConn = null;
        PreparedStatement pst = null;
        ResultSet myRs = null;

        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/bank?verifyServerCertificate=false&useSSL=true";
        String USER = "root";
        String PASS = "root";

        try {

            myConn = DriverManager.getConnection(DB_URL, USER, PASS);

            String query = "Update account set account_balance = account_balance+ ? where account_id = ?";
            pst = myConn.prepareStatement(query);
            pst.setFloat(1, sum);
            pst.setInt(2, acc_id);

            int action = pst.executeUpdate();

            if (action > 0) {
                deposit = 1;

            } else {
                deposit = 0;
            }
            return deposit;

        } catch (Exception exc) {
            exc.printStackTrace();
            return deposit;
        }
    }

    //function that take money from account id
    public static int withdraw(int acc_id, float sum) {
        int withdraw = 0;
        Connection myConn = null;
        PreparedStatement pst = null;
        ResultSet myRs = null;

        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/bank?verifyServerCertificate=false&useSSL=true";
        String USER = "root";
        String PASS = "root";

        try {

            myConn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "Select * from account where account_id =" + acc_id;
            pst = myConn.prepareStatement(query);

            float balance;
            ResultSet rs = pst.executeQuery(query);

            while (rs.next()) {
                balance = rs.getFloat("account_balance");

                if (balance < sum) {
                    withdraw = -1;
                    return withdraw;
                } else {
                    query = "Update account set account_balance = account_balance - ? where account_id = ?";
                    pst = myConn.prepareStatement(query);
                    pst.setFloat(1, sum);
                    pst.setInt(2, acc_id);
                    int action = pst.executeUpdate();
                    if (action > 0) {
                        withdraw = 1;
                        return withdraw;
                    } else {
                        withdraw = 0;
                        return withdraw;
                    }

                }

            }
            return withdraw;
        } catch (Exception exc) {
            exc.printStackTrace();
            withdraw = 0;
            return withdraw;
        }
    }

    // return account balance if you have account id
    public static Float getAccountBalance(int acc_id) {

        Connection myConn = null;
        PreparedStatement pst = null;
        ResultSet myRs = null;

        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/bank?verifyServerCertificate=false&useSSL=true";
        String USER = "root";
        String PASS = "root";

        try {

            myConn = DriverManager.getConnection(DB_URL, USER, PASS);

            String query = "Select * from account where account_id =" + acc_id;
            pst = myConn.prepareStatement(query);

            float balance;
            ResultSet rs = pst.executeQuery(query);

            while (rs.next()) {
                balance = rs.getFloat("account_balance");
                return balance;
            }
            return Float.parseFloat("0");
            

        } catch (Exception exc) {
            exc.printStackTrace();
            return Float.parseFloat("0");
        }

    }

    //print all ccounts for one bank
    public static void getAccounts(String bank) {

        Connection myConn = null;
        PreparedStatement pst = null;
        ResultSet myRs = null;

        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/bank?verifyServerCertificate=false&useSSL=true";
        String USER = "root";
        String PASS = "root";

        try {

            myConn = DriverManager.getConnection(DB_URL, USER, PASS);
            //because i didn't put bank_id and doesn't connect all  3 tables by id i'm doing left join to get all accounts for one bank 
            String query = "SELECT bank.bank_name, account.account_id, account.user_name, account.account_balance"
                    + " FROM bank"
                    + " LEFT JOIN account ON bank.account_id = account.account_id"
                    + " WHERE bank.bank_name = ?";

            pst = myConn.prepareStatement(query);
            pst.setString(1, bank);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("List of the acouns for " + bank + " are:\n");
                int acc_id = rs.getInt("account_id");
                String name = rs.getString("user_name");
                Float account_balance = rs.getFloat("account_balance");
                System.out.println(acc_id + " " + name + " " + account_balance + "\n");
            } else {
                System.out.println("There is no bank account created yet for  " + bank + " bank name!\n");
            }

            while (rs.next()) {
                int acc_id = rs.getInt("account_id");
                String name = rs.getString("user_name");
                Float account_balance = rs.getFloat("account_balance");
                System.out.println(acc_id + " " + name + " " + account_balance + "\n");
            }

        } catch (Exception exc) {
            exc.printStackTrace();

        }
    }

    //print all transaction info for one specific account
    public static void getTransaction(int acc_id) {

        Connection myConn = null;
        PreparedStatement pst = null;
        ResultSet myRs = null;

        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/bank?verifyServerCertificate=false&useSSL=true";
        String USER = "root";
        String PASS = "root";

        try {

            myConn = DriverManager.getConnection(DB_URL, USER, PASS);

            String query = "SELECT  account.account_id, transaction.id, transaction.amount, transaction.originating_account_id, transaction.resulting_account_id, transaction.transaction_reason, transaction.transaction_fee"
                    + " FROM transaction"
                    + " LEFT JOIN account ON transaction.originating_account_id = account.account_id OR transaction.resulting_account_id = account.account_id"
                    + " WHERE account.account_id = ?";

            pst = myConn.prepareStatement(query);
            pst.setInt(1, acc_id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("List of the transaction for account ID:" + acc_id + " are:\n");
                int id = rs.getInt("id");
                float amount = rs.getFloat("amount");
                int original_id = rs.getInt("originating_account_id");
                int result_id = rs.getInt("resulting_account_id");
                String reason = rs.getString("transaction_reason");
                float fee = rs.getFloat("transaction_fee");
                System.out.println(id + " " + amount + " " + original_id + " " + result_id + " " + reason + " " + fee + "\n");
            } else {
                System.out.println("There is no transaction fpr this " + acc_id + " account!\n");
            }

            while (rs.next()) {
                int id = rs.getInt("id");
                float amount = rs.getFloat("amount");
                int original_id = rs.getInt("originating_account_id");
                int result_id = rs.getInt("resulting_account_id");
                String reason = rs.getString("transaction_reason");
                float fee = rs.getFloat("transaction_fee");
                System.out.println(id + " " + amount + " " + original_id + " " + result_id + " " + reason + " " + fee + "\n");
            }

        } catch (Exception exc) {
            exc.printStackTrace();

        }
    }

    //get bank fee for one bank(if you have account id, it will return flat fee value in dolar or procent fee in procent 
    //depend of sended string
    public static int getBankFee(int acc_id, String fee) {
        Connection myConn = null;
        PreparedStatement pst = null;
        ResultSet myRs = null;

        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/bank?verifyServerCertificate=false&useSSL=true";
        String USER = "root";
        String PASS = "root";

        try {

            myConn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 3. Execute SQL query
            String query = "Select transaction_flat_fee_amount, transaction_procent_fee_value from bank where account_id =" + acc_id;
            // create the java statement
            pst = myConn.prepareStatement(query);

            float balance;
            // execute the query, and get a java resultset
            ResultSet rs = pst.executeQuery(query);

            // iterate through the java resultset
            int flat_fee = 0;
            int procent_fee = 0;

            if (rs.next()) {
                flat_fee = rs.getInt("transaction_flat_fee_amount");
                procent_fee = rs.getInt("transaction_procent_fee_value");
            }
            if (fee == "flat") {
                return flat_fee;
            } else {
                return procent_fee;
            }

        } catch (Exception exc) {
            exc.printStackTrace();
            return 0;
        }
    }

    //return total fee for specific bank
    public static float getBankTotalFee(String bank) {
        float fee = 0;
        Connection myConn = null;
        PreparedStatement pst = null;
        ResultSet myRs = null;

        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/bank?verifyServerCertificate=false&useSSL=true";
        String USER = "root";
        String PASS = "root";

        try {

            myConn = DriverManager.getConnection(DB_URL, USER, PASS);

            // left join over 3 table to find all account for the bank and all transaction for each of the account
            String query = "SELECT  bank.bank_name, account.account_id, transaction.originating_account_id, transaction.resulting_account_id, transaction.transaction_fee"
                    + " FROM bank"
                    + " LEFT  JOIN account ON bank.account_id = account.account_id "
                    + " LEFT JOIN transaction ON transaction.originating_account_id = account.account_id OR transaction.resulting_account_id = account.account_id"
                    + " WHERE bank.bank_name = ? AND transaction.transaction_fee != ?";

            pst = myConn.prepareStatement(query);
            pst.setString(1, bank);
            pst.setFloat(2, Float.parseFloat("0"));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                fee += rs.getFloat("transaction_fee");
            }
            return fee;

        } catch (Exception exc) {
            exc.printStackTrace();
            return fee;
        }

    }

    //return total transfer amount for specific bank
    public static float getBankTotalTransfer(String bank) {
        float transfer = 0;
        Connection myConn = null;
        PreparedStatement pst = null;
        ResultSet myRs = null;

        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/bank?verifyServerCertificate=false&useSSL=true";
        String USER = "root";
        String PASS = "root";

        try {

            myConn = DriverManager.getConnection(DB_URL, USER, PASS);
            //left join over 3 table to sum the transfer amount for the bank
            String query = "SELECT  bank.bank_name, account.account_id, transaction.originating_account_id, transaction.resulting_account_id, transaction.amount"
                    + " FROM bank"
                    + " LEFT  JOIN account ON bank.account_id = account.account_id "
                    + " LEFT JOIN transaction ON transaction.originating_account_id = account.account_id OR transaction.resulting_account_id = account.account_id"
                    + " WHERE bank.bank_name = ? AND transaction.amount != ?";

            pst = myConn.prepareStatement(query);
            pst.setString(1, bank);
            pst.setFloat(2, Float.parseFloat("0"));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                transfer += rs.getFloat("amount");
            }
            return transfer;

        } catch (Exception exc) {
            exc.printStackTrace();
            return transfer;
        }
    }

    //do the transfer from one to another account with using withdraw and deposit
    public static boolean transferMoney(int from_acc, int to_acc, float amount) {
        boolean transfer = false;
        int withdraw = withdraw(from_acc, amount);
        //if it can't be taken out there is no sense to do the deposit
        if (withdraw == 1) {
            int depo = deposit(to_acc, amount);
            if (depo == 1) {
                transfer = true;
            } else {
                transfer = false;
            }
        }
        return transfer;
    }

    //All console application
    public static void main(String arg[]) {
        Scanner KB = new Scanner(System.in);

        //create initial account and bank fisrt just to make sure that we have at least one bank or account
        System.out.println("Enter account id:");
        int acc_id = 0;
        try {
            acc_id = Integer.parseInt(KB.nextLine());
        }catch (NumberFormatException nfe) {
            System.out.print("Because your acc id is not rgular number the action will be killed\n"); 
            return;
        }
        System.out.println("Enter name: ");
        //String name = KB.nextLine();
        String name = "";
        try {
            name = KB.nextLine();
        }catch (NumberFormatException nfe) {
            System.out.print("Because your name  is not valid the action will be killed\n"); 
            return;
        }
        System.out.println("Enter account balance");
        float balance=0;
        
        try {
            balance = Float.parseFloat(KB.nextLine());
        }catch (NumberFormatException nfe) {
            System.out.print("Because your input was not valid your account balance is set to 0\n");   
        }
        
        //Float balance = Float.parseFloat(KB.nextLine());
        Account A = new Account(acc_id, name, balance);

        System.out.println("Enter Bank Name: ");
        String bank_name = KB.nextLine();
       // System.out.println("Enter total_transaction_fee_amount: ");
        Float total_fee = Float.parseFloat("0");
        //System.out.println("Enter total_transfer_amount");
        Float total_transfer_amount = Float.parseFloat("0");
        System.out.println("Enter transaction_flat_fee_amount(regular number-integer)");
        int transaction_flat_fee_amount = 0;
        try {
            transaction_flat_fee_amount = Integer.parseInt(KB.next());
        }catch (NumberFormatException nfe) {
            System.out.print("Because your input was not valid integer please try again from begining\n");  
            return;
        }
        System.out.println("Enter transaction_procent_fee_value");
        //int transaction_procent_fee_value = Integer.parseInt(KB.next());
        int transaction_procent_fee_value = 0;
        try {
            transaction_procent_fee_value = Integer.parseInt(KB.next());
        }catch (NumberFormatException nfe) {
            System.out.print("Because your input was not valid integer please try again \n");  
            return;
        }
        System.out.println("How many account you want to add : ");
        //int accounts = Integer.parseInt(KB.next());
        int accounts = 0;
        try {
            accounts = Integer.parseInt(KB.next());
        }catch (NumberFormatException nfe) {
            System.out.print("Because your input was not valid the action will killed \n");
            return;
        }
        ArrayList<Integer> listAccount = new ArrayList<Integer>();
        for (int x = 0; x < accounts; x++) {
            System.out.println("Add account id : ");
           // int bank_account = Integer.parseInt(KB.next());
            int bank_account = 0;
            try {
                bank_account = Integer.parseInt(KB.next());
            }catch (NumberFormatException nfe) {
                System.out.print("Because your input was not valid the action will killed \n");
                return;
            }
            listAccount.add(bank_account);
        }
        Bank C = new Bank(bank_name, listAccount, total_transfer_amount, total_fee, transaction_flat_fee_amount, transaction_procent_fee_value);

        //run loop until menu 11 is not pressed
        int ch;
        do {
            System.out.println("1.Create Bank\n"
                    + "2.Create Account\n "
                    + "3.Transfer Money from one account to another\n"
                    + "4.Deposit\n"
                    + "5.Withdrawal\n"
                    + "6.Check account balance for any account\n"
                    + "7.List of Bank account\n"
                    + "8.List of transaction per account\n"
                    + "9.Check bank totat transaction fee amount\n"
                    + "10.Check bank total transfer ammount\n"
                    + "11.Exit");
            System.out.println("Ur Choice :");
           // ch = KB.nextInt();
            ch = 0;
            try {
                ch = Integer.parseInt(KB.next());
            }catch (NumberFormatException nfe) {
                System.out.print("Because your input was not valid the action will killed \n");
                return;
            }
            
            switch (ch) {
                case 1:
                    KB.nextLine();
                    System.out.println("Enter Bank Name: ");
                    bank_name = KB.nextLine();
                    //System.out.println("Enter total_transaction_fee_amount: ");
                    total_fee = Float.parseFloat("0");
                    //System.out.println("Enter total_transfer_amount");
                    total_transfer_amount = Float.parseFloat("0");
                    System.out.println("Enter transaction_flat_fee_amount");
                    //transaction_flat_fee_amount = Integer.parseInt(KB.next());
                    try {
                        transaction_flat_fee_amount = Integer.parseInt(KB.next());
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    System.out.println("Enter transaction_procent_fee_value");
                    //transaction_procent_fee_value = Integer.parseInt(KB.next());
                    try {
                        transaction_procent_fee_value = Integer.parseInt(KB.next());
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    System.out.println("How many account you want to add : ");
                    //accounts = Integer.parseInt(KB.next());
                    try {
                        accounts = Integer.parseInt(KB.next());
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    //ArrayList<Integer> listAccount = new ArrayList<Integer>();
                    for (int x = 0; x < accounts; x++) {
                        System.out.println("Add account id : ");
                        int bank_account = 0;
                        try {
                            bank_account = Integer.parseInt(KB.next());
                        }catch (NumberFormatException nfe) {
                            System.out.print("Because your input was not valid the action will killed \n");
                            continue;
                        }
                        listAccount.add(bank_account);
                    }
                    C = new Bank(bank_name, listAccount, total_transfer_amount, total_fee, transaction_flat_fee_amount, transaction_procent_fee_value);
                    break;

                case 2:
                    System.out.println("Enter Account id: ");
                    //acc_id = Integer.parseInt(KB.next());
                    try {
                        acc_id = Integer.parseInt(KB.next());
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    System.out.println("Enter name: ");
                    name = KB.nextLine();
                    System.out.println("Enter account balance");
                    //balance = Float.parseFloat(KB.next());
                    try {
                        balance = Float.parseFloat(KB.next());
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    A = new Account(acc_id, name, balance);

                    break;

                case 3:
                    System.out.print("Enter account id from where you will transfer money : ");
                    int from_acc = 0;
                    try {
                        from_acc = Integer.parseInt(KB.next());
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    System.out.print("Enter account id where you will transfer the money : ");
                    //int to_acc = Integer.parseInt(KB.next());
                    int to_acc = 0;
                    try {
                        to_acc = Integer.parseInt(KB.next());
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    System.out.print("Enter amount that should be transfered : ");
                   // float amount = Float.parseFloat(KB.next());
                    float amount = 0;
                    try {
                        amount = Float.parseFloat(KB.next());
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    boolean transfer = transferMoney(from_acc, to_acc, amount);
                    if (transfer = true) {
                        System.out.print("Your transfer was successfully made.\n");
                        int bank_fee = getBankFee(from_acc, "procent");
                        float transfer_fee = (amount * bank_fee) / 100;
                        Transaction t = new Transaction(amount, from_acc, to_acc, "transfer", transfer_fee);
                    } else {
                        System.out.print("There is no enought funds on origin account");
                    }
                    break;

                case 4:
                    System.out.print("Enter Account No : ");
                    int acn = Integer.parseInt(KB.next());
                    
                    System.out.print("Enter deposit: ");
                    //float my_deposit = Float.parseFloat(KB.next());
                    float my_deposit = 0;
                    try {
                        my_deposit = Float.parseFloat(KB.next());
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    boolean found = false;

                    found = getAccount(acn);
                    if (found) {

                        if (deposit(acn, my_deposit) == 1) {
                            System.out.println("Your deposit was successfully added to your account.\n");
                            //a got account id, find fte bank from where this account is and take that bank flat fee
                            int bank_fee = getBankFee(acn, "flat");
                            
                            Transaction t = new Transaction(my_deposit, 0, acn, "deposit", bank_fee);
                        } else {
                            System.out.println("An error occured, try again");
                        }

                    }

                    if (!found) {
                        System.out.println("Search Failed..Account Not Exist..\n");
                    }
                    break;

                case 5:
                    System.out.print("Enter Account No : ");
                    //acn = Integer.parseInt(KB.next());
                    
                    try {
                        acn = Integer.parseInt(KB.next());
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    System.out.print("How much money you are taking out? ");
                    //float my_amount = Float.parseFloat(KB.next());
                    float my_amount = 0;
                    try {
                        my_amount = Float.parseFloat(KB.next());;
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    found = false;

                    found = getAccount(acn);
                    if (found) {

                        switch (withdraw(acn, my_amount)) {
                            case 0:
                                System.out.println("An error occured, try again.\n");
                                break;
                            case 1:
                                System.out.println("Please take your money. Thanks for choosing us.\n");
                                break;
                            case -1:
                                System.out.println("Your balance is less than requested amount.\n");
                                break;
                        }
                        int bank_fee = getBankFee(acn, "flat");
                        Transaction t = new Transaction(my_amount, 0, acn, "withdraw", bank_fee);
                    } else {

                        System.out.println("Search Failed..Account Not Exist..");
                    }
                    break;
                case 6:
                    System.out.print("Check account balance for account_id  : ");
                    //acn = Integer.parseInt(KB.next());
                    try {
                        acn = Integer.parseInt(KB.next());
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    found = false;
                    found = getAccount(acn);
                    if (found) {
                        float my_balance = getAccountBalance(acn);
                        if (my_balance == Float.parseFloat("0")) {
                            System.out.print("Error");
                        } else {
                            System.out.print("Balance for this account is " + my_balance + "\n");
                        }
                    } else {
                        System.out.print("Account with that id does not exist\n");
                    }
                    break;
                case 7:
                    KB.nextLine();
                    System.out.print("Enter Bank Name that you nead list of account : ");
                    String my_bank_name = KB.nextLine();
                    getAccounts(my_bank_name);
                    break;
                case 8:
                    System.out.print("Enter account id for which you need transaction : ");
                    //acn = Integer.parseInt(KB.next());
                    try {
                        acn = Integer.parseInt(KB.next());
                    }catch (NumberFormatException nfe) {
                        System.out.print("Because your input was not valid the action will killed \n");
                        return;
                    }
                    getTransaction(acn);
                    break;
                case 9:
                    KB.nextLine();
                    System.out.print("Enter bank Name for which want to check total transaction fee : ");
                    String my_bn = KB.nextLine();
                    float bank_fee = getBankTotalFee(my_bn);
                    System.out.print(my_bn + " total transaction fee are " + bank_fee + "$\n");
                    break;
                case 10:
                    KB.nextLine();
                    System.out.print("Enter bank Name for which want to check total transfer amount : ");
                    String my_bank = KB.nextLine();
                    float bank_transfer = getBankTotalTransfer(my_bank);
                    System.out.print(my_bank + " has total transfer amount of" + bank_transfer + "$\n");
                    break;
                case 11:
                    System.out.print("Good Buy");
                    break;
            }
        } while (ch != 11);
    }
    
    
}
