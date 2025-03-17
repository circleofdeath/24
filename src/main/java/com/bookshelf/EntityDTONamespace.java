package com.bookshelf;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

public class EntityDTONamespace {
    @Data
    @Entity
    @Table(name = "users")
    public static class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String username;
        private String password;
        private String salt;
        private String token;
    }

    @Data
    @Entity
    @Table(name = "books")
    public static class Book {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private Long author;
        private String content;
    }

    @Data
    public static class BookCreation {
        private Auth auth;
        private String name;
        private String content;
    }

    @Data
    public static class Auth {
        private String username;
        private String password;
        private String token;

        public boolean isTokenAuth() {
            return token != null;
        }

        public boolean isLoginAuth() {
            return password != null && username != null;
        }

        public boolean isInvalid() {
            return username == null && password == null && !isTokenAuth();
        }
    }
}
