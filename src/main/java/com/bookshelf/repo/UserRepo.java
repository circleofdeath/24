package com.bookshelf.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookshelf.EntityDTONamespace.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> 
{   public User findByEmail(String email)
;
}