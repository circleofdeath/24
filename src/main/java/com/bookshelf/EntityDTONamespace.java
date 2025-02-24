package com.bookshelf;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

public class EntityDTONamespace 
{   @Data
    @Entity
    @Table(name="users")
    public static class User 
    {   @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id
    ;   private String username
    ;   private String email
    ;   private String password
    ;   private String salt
    ;
    }
    
    @Data
    public static class UserRegistration 
    {   private String username
    ;   private String email
    ;   private String password
    ;
    }

    @Data
    public static class UserLogin
    {   private String email
    ;   private String password
    ;
    }
}