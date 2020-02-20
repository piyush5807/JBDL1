package com.example.gWallet.Repository;

import com.example.gWallet.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}

