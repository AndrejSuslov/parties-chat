package com.ddc.chat.repository;

import com.ddc.chat.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {

    Users findByUsername(String username);

    Users findByEmail(String email);
}
