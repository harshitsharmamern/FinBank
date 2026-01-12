package com.example.bank.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.bank.Dto.CreateAccount;
import com.example.bank.Dto.Loginrequestbody;
import com.example.bank.Entity.Transaction;
import com.example.bank.Entity.User;
import com.example.bank.Repository.UserRepo;
import com.example.bank.Security.JwtUtils;
import com.example.bank.Services.UserService;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;



@CrossOrigin(origins = "*")
@RestController
public class user {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils; 

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepo UserRepo;


    @GetMapping("/testing")
   public List<CreateAccount> testing(){ 
       return userService.getAllusers();
   }
   @PostMapping("/login")
   public ResponseEntity<?> LoginMethod(@RequestBody Loginrequestbody loginrequestbody){ 
    
    try{

        // User user = userService.login(loginrequestbody);
        // User user = UserRepo.findByAccountNumber(loginrequestbody.getAccountNumber()).orElse(null);
        // if(user == null){
        //     throw new RuntimeException("User not found with account number: " + loginrequestbody.getAccountNumber());
        // }
        Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginrequestbody.getAccountNumber(),  
            // i have to change cause in custorm userdetail i will use or set username
            // user.getUsername(),
            loginrequestbody.getPassword()
        )
    );
    String jwtToken = jwtUtils.generateToken(authentication);


        HashMap<String, Object> response = new HashMap<>();
        
        response.put("status", "success");
        response.put("token", jwtToken);
        return ResponseEntity.ok(response);
    }catch(Exception e){
        throw new RuntimeException(e.getMessage());
    }
   }



   @PostMapping("/CreateAccount")
   public ResponseEntity<?> createAccount(@RequestBody CreateAccount createAccount) {
    System.out.println(createAccount);
    HashMap<String, Object> response = new HashMap<>();
    try{

        CreateAccount newUser  = userService.CreateAccount(createAccount);
        
        String token = jwtUtils.generateTokenForNewUser(newUser.getAccountNumber());
        
        response.put("status", "success");
        response.put("data", newUser);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }catch(Exception e){
        throw new RuntimeException(e.getMessage());
        // response.put("message", e.getMessage());
        // return ResponseEntity.status(500).body(response);
    }
   }

   @PostMapping("/transfer-money")
   public ResponseEntity<?> transferMoney(@RequestBody HashMap<String, Object> transferRequest) {
    // Implement money transfer logic here
    String fromAccount = (String) transferRequest.get("sourceAccountId");
    String toAccount = (String) transferRequest.get("targetAccountId");
    Double amount = Double.valueOf(transferRequest.get("amount").toString());
// sourceAccountId: '',
//         targetAccountId: '',
//         amount: ''
    try{
        HashMap<String, Object> user = userService.transferMoney(fromAccount, toAccount, amount);
    
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Money transferred successfully");
        response.put("data", user);
        return ResponseEntity.ok(response);
    }catch(Exception e){
        throw new RuntimeException(e.getMessage());
    }
   }

   @GetMapping("/loggedin_user")
   public ResponseEntity<?> getLoginUser(@AuthenticationPrincipal UserDetails userDetails){ 
    // i want full details of loggin user   
    HashMap<String, Object> response = new HashMap<>();
    try{
        User user = UserRepo.findByAccountNumber(userDetails.getUsername()).orElse(null);
        if(user==null){
            // response.put('status', 'error');
            // response.put('message', 'User not found');

            throw new RuntimeException("User not found with account number: " + userDetails.getUsername());
        }
        response.put("status", "success");
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


    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@AuthenticationPrincipal UserDetails userDetails) {
       // i want to get account number from jwt token
        // Double balance = userService.getBalance(accountNumber);
        
        String accountNumber = userDetails.getUsername();

        Double balance = userService.getBalance(accountNumber);

        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("accountNumber", accountNumber);
        response.put("balance", balance);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> postMethodName(@RequestBody HashMap<String, String> request) {
        
        String accountNumber = request.get("accountNumber");
        Double amount = Double.parseDouble(request.get("amount"));
        CreateAccount user = userService.depositMoney(accountNumber, amount);
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getMethodName(@RequestParam String accountNumber) {

        HashMap<String, Object> response = new HashMap<>();
        List<Transaction> transactions = userService.getTransactions(accountNumber);
        response.put("status", "success");
        response.put("data", transactions);
        return ResponseEntity.ok(response);
    }
    

   
    
   
}
   
