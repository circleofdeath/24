package com.bookshelf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class BookshelfApplication {
    public static String readPageAndSetContent(String content) {
        return readFile("/index.html").replace("<D!HTML-TEMPLATE-14500$>", content);
    }

    public static String readFile(String location) {
        try(var stream = new BufferedReader(new InputStreamReader(BookshelfApplication.class.getResourceAsStream(location)))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while((line = stream.readLine()) != null) {
                builder.append(line).append(System.lineSeparator());
            }
            return builder.toString();
        } catch(Throwable e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "<h1>ERROR LOADING RESOURCE:</h1><br>" + sw.toString();
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BookshelfApplication.class, args);
    }

    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for(byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}