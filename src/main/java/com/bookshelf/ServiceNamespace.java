package com.bookshelf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookshelf.EntityDTONamespace.User;
import com.bookshelf.repo.UserRepo;

import java.util.List;

@Service
public class ServiceNamespace
{   @Autowired UserRepo userRepo
    
;   public User register(String username, String email, String password) 
    {   User user = new User()
    ;   user.setEmail(email)
    ;   user.setId(null)
    ;   user.setUsername(username)
    ;   String salt = BookshelfApplication.sha256(email + username + System.nanoTime())
    ;   user.setSalt(salt)
    ;   user.setPassword(BookshelfApplication.sha256(salt + password + salt + password + salt))
    ;   userRepo.save(user)
    ;   return user
    ;
    }

    public List<User> getAllUsers() 
    {   return userRepo.findAll()
    ;
    }

    public boolean loginSuccessful(String email, String password)
    {   User user = userRepo.findByEmail(email)
        
    ;   if (user == null) 
        {   return false
        ;
        }

    ;   String salt = user.getSalt()
    ;   String hashedPassword = BookshelfApplication.sha256(salt + password + salt + password + salt)
    ;   return hashedPassword.equals(user.getPassword())
    ;
    }

    public String getUsername(String email)
    {   User user = userRepo.findByEmail(email)
    ;   return user == null ? null : user.getUsername()
    ;
    }
}