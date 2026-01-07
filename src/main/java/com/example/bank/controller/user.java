package com.example.bank.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.bank.Dto.CreateAccount;
import com.example.bank.Dto.Loginrequestbody;
import com.example.bank.Services.UserService;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
   public String LoginMethod(@RequestBody Loginrequestbody loginrequestbody){ 
       return userService.login(loginrequestbody);
   }

   @PostMapping("/CreateAccount")
   public ResponseEntity<HashMap<String, Object>> createAccount(@RequestBody CreateAccount createAccount) {
    System.out.println(createAccount);
    HashMap<String, Object> response = new HashMap<>();

    CreateAccount user  = userService.CreateAccount(createAccount);
    if(user!=null){
        response.put("data", user);
        response.put("message", "success");
    }else{
        response.put("data", null);
        response.put("message", "failed");
    }
        return ResponseEntity.ok(response);
   }
   
}
   
