package com.example.bank.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bank.Entity.User;

import java.lang.StackWalker.Option;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    Optional<User> findByAccountNumber(String accountNumber);
    Optional<User> findByAadharNumber(String AadharNumber);
    Optional<User> findByPanNumber(String panNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
}
