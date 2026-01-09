package com.example.bank.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.bank.Dto.CreateAccount;
import com.example.bank.Dto.Loginrequestbody;
import com.example.bank.Entity.User;
import com.example.bank.Services.UserService;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
public class user {

    @Autowired
    private UserService userService;
    @GetMapping("/testing")
   public List<CreateAccount> testing(){ 
       return userService.getAllusers();
   }
   @PostMapping("/login")
   public ResponseEntity<?> LoginMethod(@RequestBody Loginrequestbody loginrequestbody){ 
        User user = userService.login(loginrequestbody);
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", user);
        return ResponseEntity.ok(response);
   }

   @PostMapping("/CreateAccount")
   public ResponseEntity<?> createAccount(@RequestBody CreateAccount createAccount) {
    System.out.println(createAccount);
    CreateAccount user  = userService.CreateAccount(createAccount);
    HashMap<String, Object> response = new HashMap<>();
    response.put("status", "success");
    response.put("data", user);
    return ResponseEntity.ok(response);
   }

   @PostMapping("/transfer-money")
   public ResponseEntity<?> transferMoney(@RequestBody HashMap<String, Object> transferRequest) {
    // Implement money transfer logic here
    String fromAccount = (String) transferRequest.get("fromAccount");
    String toAccount = (String) transferRequest.get("toAccount");
    Double amount = Double.valueOf(transferRequest.get("amount").toString());

    try{
        CreateAccount user = userService.transferMoney(fromAccount, toAccount, amount);
    
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Money transferred successfully");
        response.put("data", user);
        return ResponseEntity.ok(response);
    }catch(Exception e){
        throw new RuntimeException(e.getMessage());
    }
   }

   @DeleteMapping("/delete-accounts")
    public String deleteAllAccounts() {
         userService.deleteAllAccounts();
         return "All accounts deleted successfully.";
    }
    
   
}
   
