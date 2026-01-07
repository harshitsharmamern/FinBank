package com.example.bank.Dto;

public class CreateAccount {
    private String AccountNumber;
    
    private String username;
    private String phoneNumber;
    private String DOB;
    private String email;
    private String address;
    private String AadharNumber;
    private String panNumber;
    private String password;

    private String accountType;
    private Double initialDeposit;

    private String Currentballance;


    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.AccountNumber = accountNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAadharNumber() {
        return AadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.AadharNumber = aadharNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(Double initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public String getCurrentballance() {
        return Currentballance;
    }

    public void setCurrentballance(String currentballance) {
        this.Currentballance = currentballance;
    }

    @Override
    public String toString() {
        return "CreateAccount [AccountNumber=" + AccountNumber + ", username=" + username + ", phoneNumber="
                + phoneNumber + ", DOB=" + DOB + ", email=" + email + ", address=" + address + ", AadharNumber="
                + AadharNumber + ", panNumber=" + panNumber + ", password=" + password + ", accountType=" + accountType
                + ", initialDeposit=" + initialDeposit + ", Currentballance=" + Currentballance + "]";
    }    
}
