package com.example.bank.Services;

import com.example.bank.Dto.Loginrequestbody;
import com.example.bank.Dto.CreateAccount;

public interface UserServiceInterface {
    public String login(Loginrequestbody loginrequestbody);
    public CreateAccount CreateAccount(CreateAccount createAccount);
}
