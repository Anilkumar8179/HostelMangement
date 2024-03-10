package com.example.hostelMangement.Repository;

import com.example.hostelMangement.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User ,Long>{
    User findByFullnameAndPassword(String fullname, String password);
    User findByEmail(String fullname);
    User findByToken(String token);


}
