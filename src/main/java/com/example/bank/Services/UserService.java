package com.example.bank.Services;

import com.example.bank.Dto.Loginrequestbody;
import com.example.bank.Entity.User;
import com.example.bank.Repository.UserRepo;
import com.example.bank.controller.user;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
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
    public String login(Loginrequestbody loginrequestbody) {
        String aadhar = loginrequestbody.getAccountNumber();
        String password = loginrequestbody.getPassword();
        try{

            User user = userRepo.findByAccountNumber(aadhar).orElse(null);
            if(user==null){
                return "User Not Found";
            }
            if(user.getPassword().equals(password)){
                return "Login Successful";
            }
            return "Invalid Credentials";
        }
        catch(Exception e){
            // System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public CreateAccount CreateAccount(CreateAccount createAccount)  {
        User user = new User();
        try{

            //i want to check first is Aadhar phonenumber email pan is already exist or not
            String aadhar = createAccount.getAadharNumber();
            // String email = createAccount.getEmail();
            String pan = createAccount.getPanNumber();
            String phone = createAccount.getPhoneNumber();
            
            if (userRepo.findByAadharNumber(aadhar).isPresent()) {
                 throw new Exception("Aadhar number already exists");
                // return null;
                // return "Aadhar number already exists";
        }
        if (userRepo.findByPanNumber(createAccount.getPanNumber()).isPresent()) {
            throw new Exception("PAN number already exists");
            // return null;
            // return "PAN number already exists";
        }
        if (userRepo.findByPhoneNumber(createAccount.getPhoneNumber()).isPresent()) {
            throw new Exception("Phone number already exists");

            // return null;
            // return "Phone number already exists";
        }
        if (userRepo.findByEmail(createAccount.getEmail()).isPresent()) {
            throw new Exception("Email already exists");
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
        

        var data = userRepo.save(user);

        return modelMapper.map(data, CreateAccount.class);

        // return "User Created Successfully";
    }
    catch(Exception e){
        System.out.println(e.getMessage());
        return null;
    }
    
    }
}
