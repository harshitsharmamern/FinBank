package com.example.bank.Services;

import com.example.bank.Dto.Loginrequestbody;
import com.example.bank.Dto.TransactionDto;
import com.example.bank.Entity.Transaction;
import com.example.bank.Entity.User;
import com.example.bank.Exception.UserException;
import com.example.bank.Repository.TransactionRepo;
import com.example.bank.Repository.UserRepo;
import com.example.bank.controller.user;

import io.micrometer.core.ipc.http.HttpSender.Response;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bank.Dto.CreateAccount;

@Service
public class UserService implements UserServiceInterface{

    // /*~~(Unable to determine parameter type)~~>*/private final controller.user user;

    private final ModelMapper modelMapper;
    
    @Autowired
    private UserRepo userRepo;
        @Autowired
    TransactionRepo transactionRepo;

    UserService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<CreateAccount> getAllusers() {
        List<User> Alluser =  userRepo.findAll();
        List<CreateAccount> createAccounts = new ArrayList<>();

        Alluser.forEach(user -> {
            createAccounts.add(modelMapper.map(user, CreateAccount.class));
        });
        return createAccounts;
    }
    @Override
    public User login(Loginrequestbody loginrequestbody) {
        String accountnum = loginrequestbody.getAccountNumber();
        String password = loginrequestbody.getPassword();
        try{

            User user = userRepo.findByAccountNumber(accountnum).orElse(null);
            if(user==null){
                throw new UserException("Account Number does not exist");
            }
            if(user.getPassword().equals(password)){
                return user;
            }
            else{
                throw new UserException("Incorrect Password");
            }
        }
        catch(Exception e){
            // System.out.println(e.getMessage());
            throw new UserException(e.getMessage());      }
    }

    @Override
    @Transactional
    public CreateAccount CreateAccount(CreateAccount createAccount)  {
        User user = new User();
     
            //i want to check first is Aadhar phonenumber email pan is already exist or not
            String aadhar = createAccount.getAadharNumber();
            // String email = createAccount.getEmail();
            String pan = createAccount.getPanNumber();
            String phone = createAccount.getPhoneNumber();
            
            if (userRepo.findByAadharNumber(aadhar).isPresent()) {
                 throw new UserException("Aadhar number already exists");
                
                 // return null;
                // return "Aadhar number already exists";
        }
        if (userRepo.findByPanNumber(createAccount.getPanNumber()).isPresent()) {
            throw new UserException("PAN number already exists");
            // return null;
            // return "PAN number already exists";
        }
        if (userRepo.findByPhoneNumber(createAccount.getPhoneNumber()).isPresent()) {
            throw new UserException("Phone number already exists");

            // return null;
            // return "Phone number already exists";
        }
        if (userRepo.findByEmail(createAccount.getEmail()).isPresent()) {
            throw new UserException("Email already exists");
            // return null;
            // return "Email already exists";
        }
        // genrate accountnumber
        String accountNumber = pan.substring(0, 4) + phone.substring(6,10) + aadhar.substring(0,4);
        
        while(userRepo.findByAccountNumber(accountNumber).isPresent()){
            int randomNum = (int)(Math.random() * 9000) + 1000; // Generate a random 4-digit number
            accountNumber = pan.substring(0, 4) + phone.substring(6,10) + String.valueOf(randomNum);
        }

        user.setAccountNumber(accountNumber);
        user.setUsername(createAccount.getUsername());
        user.setPhoneNumber(createAccount.getPhoneNumber());
        user.setDOB(createAccount.getDOB());
        user.setEmail(createAccount.getEmail());
        user.setAddress(createAccount.getAddress());
        user.setAadharNumber(createAccount.getAadharNumber());
        user.setPanNumber(createAccount.getPanNumber());
        user.setPassword(createAccount.getPassword());
        user.setAccountType(createAccount.getAccountType());
        user.setInitialDeposit(createAccount.getInitialDeposit());
        user.setCurrentballance(createAccount.getInitialDeposit());

        TransactionDto transactions =  new TransactionDto();

        transactions.setFromAccount(user.getAccountNumber());

        transactions.setToAccount(user.getAccountNumber());
        transactions.setAmount(createAccount.getInitialDeposit());
        transactions.setTransactionType("Deposit");
        transactions.setTransactionDate(java.time.LocalDate.now().toString());
        
        Transaction transactionEntity = modelMapper.map(transactions, Transaction.class);
        transactionEntity.setUser(user);

        transactionRepo.save(transactionEntity);

        // Transaction transactionEntity = modelMapper.map(transactions, Transaction.class);
        user.setTransactions(List.of(transactionEntity));

        var data = userRepo.save(user);

        return modelMapper.map(data, CreateAccount.class);

    }


    @Transactional
    public HashMap<String, Object> transferMoney(String fromAccount, String toAccount, Double amount) {
        User user = userRepo.findByAccountNumber(fromAccount).orElse(null);

        User recipient = userRepo.findByAccountNumber(toAccount).orElse(null);
    
        if(user == null) {
            throw new UserException("Sender account not found");
        }else if(recipient==null) {
            throw new UserException("Recipient account not found");
        }

        if(user.getCurrentballance() < amount){
            throw new UserException("Insufficient balance");
        }
        try{

            // Deduct amount from sender's account
            user.setCurrentballance(user.getCurrentballance() - amount);
            
            TransactionDto transaction = new TransactionDto();
            transaction.setFromAccount(user.getAccountNumber());
            transaction.setToAccount(recipient.getAccountNumber());
            transaction.setAmount(amount);
            transaction.setTransactionType("debit");
            transaction.setTransactionDate(java.time.LocalDate.now().toString());

            // user.getTransactions().add(transaction);
            Transaction transactionEntity = modelMapper.map(transaction, Transaction.class);
            transactionEntity.setUser(user);
            transactionRepo.save(transactionEntity);

            
            user.getTransactions().add(transactionEntity);
            userRepo.save(user);

            // // Add amount to recipient's account
            recipient.setCurrentballance(recipient.getCurrentballance() + amount);
            
            // transaction = new TransactionDto();
            // transaction.setFromAccount(user.getAccountNumber());
            // transaction.setToAccount(recipient.getAccountNumber());
            // transaction.setAmount(amount);
            transaction.setTransactionType("credit");
            // transaction.setTransactionDate(java.time.LocalDate.now().toString());
            
            transactionEntity = modelMapper.map(transaction, Transaction.class);
            transactionEntity.setUser(recipient);
            recipient.getTransactions().add(transactionEntity);
            
            userRepo.save(recipient);
            HashMap<String, Object> response = new HashMap<>();
            response.put("transaction_id", transactionEntity.getTransaction_id());
            response.put("Use", user);

            return response;
        } catch(Exception e){
            throw new UserException(e.getMessage());
        }
    }

    public Double getBalance(String accountNumber){
        User user = userRepo.findByAccountNumber(accountNumber).orElse(null);
        if(user == null){
            throw new UserException("Account not found");
        }
        return user.getCurrentballance();
    }

    @Transactional
    public CreateAccount depositMoney(String accountNumber, Double amount){
        User user = userRepo.findByAccountNumber(accountNumber).orElse(null);
        if(user == null){
            throw new UserException("Account not found");
        }
        try{

            user.setCurrentballance(user.getCurrentballance() + amount);
            
            TransactionDto transaction = new TransactionDto();
            transaction.setFromAccount(accountNumber);
            transaction.setToAccount(accountNumber);
            transaction.setAmount(amount);
            transaction.setTransactionType("Deposit");
            transaction.setTransactionDate(java.time.LocalDate.now().toString());
            
            Transaction transactionEntity = modelMapper.map(transaction, Transaction.class);
            transactionEntity.setUser(user);
            transactionRepo.save(transactionEntity);
            
            user.getTransactions().add(transactionEntity);
            userRepo.save(user);

            return modelMapper.map(user, CreateAccount.class);
        } catch(Exception e){
            throw new UserException(e.getMessage());
        }
    }

    public List<Transaction> getTransactions(String accountNumber){
        User user = userRepo.findByAccountNumber(accountNumber).orElse(null);
        if(user == null){
            throw new UserException("Account not found");
        }
        List<Transaction> transactions = user.getTransactions();
        return transactions;
    }

  

    public void deleteAllAccounts(){
        userRepo.deleteAll();
    }
}
