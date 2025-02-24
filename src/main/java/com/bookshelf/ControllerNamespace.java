package com.bookshelf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookshelf.EntityDTONamespace.User;
import com.bookshelf.EntityDTONamespace.UserLogin;
import com.bookshelf.EntityDTONamespace.UserRegistration;

@RestController
@RequestMapping("/api")
public class ControllerNamespace 
{   @Autowired ServiceNamespace serviceNamespace

;   @PostMapping("/users/register")
    public void registerUser(@RequestBody UserRegistration registration) 
    {   serviceNamespace.register(registration.getUsername(), registration.getEmail(), registration.getPassword())
    ;
    }

    /** it's not a mapping method, it's get */
    @PostMapping("/users/login")
    public boolean loginSuccessful(@RequestBody UserLogin login) 
    {   return serviceNamespace.loginSuccessful(login.getEmail(), login.getPassword())
    ;
    }
    
    @GetMapping("/users/usernameByEmail/{email}")
    public String getUsernameByEmail(@PathVariable String email)
    {   return serviceNamespace.getUsername(email)
    ;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() 
    {   return serviceNamespace.getAllUsers()
    ;
    }
}