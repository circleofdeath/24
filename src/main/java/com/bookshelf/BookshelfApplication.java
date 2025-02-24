package com.bookshelf;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BookshelfApplication 
{   public static void main(String[] args) 
    {   SpringApplication.run(BookshelfApplication.class, args)
    ;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
                registry.addMapping("/api/**").allowedOrigins("*");
            }
        };
    }

	public static String sha256(String input) 
    {   try 
        {   MessageDigest digest = MessageDigest.getInstance("SHA-256")
        ;   byte[] hashBytes = digest.digest(input.getBytes())
        ;   StringBuilder hexString = new StringBuilder()

        ;   for(byte b : hashBytes) 
            {   String hex = Integer.toHexString(0xff & b)

            ;   if(hex.length() == 1) 
                {   hexString.append('0')
                ;
                }

            ;   hexString.append(hex)
            ;
			}

        ;   return hexString.toString()
        ;
        } 
        catch (NoSuchAlgorithmException e) 
        {   throw new RuntimeException("SHA-256 algorithm not found", e)
        ;
        }
    }
}