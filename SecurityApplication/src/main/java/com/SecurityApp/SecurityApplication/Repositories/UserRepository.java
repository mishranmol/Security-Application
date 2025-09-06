package com.SecurityApp.SecurityApplication.Repositories;

import com.SecurityApp.SecurityApplication.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //In week-3 we have studied that hibernate will convert it into corresponding SQL query
     Optional<User> findByEmail(String email);

}
