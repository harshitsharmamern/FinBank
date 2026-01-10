package com.example.bank.Services;

import com.example.bank.Dto.Loginrequestbody;
import com.example.bank.Entity.User;

import java.util.HashMap;

import com.example.bank.Dto.CreateAccount;

public interface UserServiceInterface {
    public User login(Loginrequestbody loginrequestbody);
    public CreateAccount CreateAccount(CreateAccount createAccount);
    public HashMap<String, Object> transferMoney(String fromAccount, String toAccount, Double amount);
}
