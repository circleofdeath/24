package com.bookshelf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookshelf.EntityDTONamespace.Auth;
import com.bookshelf.EntityDTONamespace.Book;
import com.bookshelf.EntityDTONamespace.BookCreation;
import com.bookshelf.EntityDTONamespace.User;
import com.bookshelf.repo.BookRepo;
import com.bookshelf.repo.UserRepo;

import static com.bookshelf.BookshelfApplication.*;
import org.springframework.web.bind.annotation.RequestParam;



// accessing local storage is new way of pain
@RestController
@RequestMapping
public class ControllerNamespace {
    @Autowired
    UserRepo userRepo;
    @Autowired
    BookRepo bookRepo;

    //#region page layer

    @GetMapping
    public String mainPage() {
        return readPageAndSetContent(readFile("/templates/main.html"));
    }

    @GetMapping("/login")
    public String loginPage() {
        return readPageAndSetContent(readFile("/templates/login.html"));
    }

    @GetMapping("/signup")
    public String signupPage() {
        return readPageAndSetContent(readFile("/templates/signup.html"));
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return readPageAndSetContent(readFile("/templates/dashboard.html"));
    }

    @GetMapping("/books")
    public String books() {
        StringBuilder builder = new StringBuilder("<h1>List of books</h1><p>to find books user Ctrl+F LMAO</p>");
       
        for(Book book : bookRepo.findAll()) {
            builder
                    .append("<div><h3>")
                    .append(book.getName())
                    .append("</h3><p>by ")
                    .append(userRepo.getReferenceById(book.getAuthor()).getUsername())
                    .append(", <a href=\"/r/books/")
                    .append(book.getId())
                    .append("\">Read book</a></p></div>");
        }

        return readPageAndSetContent(builder.toString());
    }

    @GetMapping("/r/books/{bookID}")
    public String bookGetByID(@PathVariable String bookID) {
        try {
            Book book = bookRepo.getReferenceById(Long.parseLong(bookID));
            return readPageAndSetContent(new StringBuilder()
                .append("<div><h1>")
                .append(book.getName())
                .append("</h1><p>by ")
                .append(userRepo.getReferenceById(book.getAuthor()).getUsername())
                .append("</p></div><br>")
                .append(book.getContent())
                .toString()
            );
        } catch(Throwable ignored) {
            return readPageAndSetContent("<h1>404: Page not found!</h1>");
        }

    }

    //#endregion
    //#region api layer
    
    @PostMapping("/service/cbook")
    public ResponseEntity<Book> createBook(@RequestBody BookCreation creation) {
        var u = login(creation.getAuth()).getBody();
        if(u != null) {
            Book book = new Book();
            book.setAuthor(u.getId());
            book.setName(creation.getName());
            book.setContent(creation.getContent());
            bookRepo.save(book);
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/service/login")
    public ResponseEntity<User> login(@RequestBody Auth auth) {
        if(auth == null) return ResponseEntity.badRequest().build();
        if(auth.isTokenAuth()) {
            User user = userRepo.findByToken(auth.getToken());
            if(user == null) {
                return ResponseEntity.status(401).build();
            } else {
                return ResponseEntity.ok(user);
            }
        } else {
            User user = userRepo.findByUsername(auth.getUsername());
            if(user == null) return ResponseEntity.notFound().build();
            String salt = user.getSalt();
            String hashedPassword = sha256(auth.getPassword() + salt);
            if(hashedPassword.equals(user.getPassword())) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(401).build();
            }
        }
    }

    @PostMapping("/service/register")
    public ResponseEntity<User> register(@RequestBody Auth auth) {
        if(auth == null || !auth.isLoginAuth()) return ResponseEntity.badRequest().build();
        if(userRepo.findByUsername(auth.getUsername()) != null) return ResponseEntity.status(401).build();
        User user = new User();
        user.setUsername(auth.getUsername());
        String salt = sha256(auth.getUsername() + System.nanoTime());
        user.setSalt(salt);
        user.setPassword(sha256(auth.getPassword() + salt));
        user.setToken(sha256(user.getUsername() + user.getPassword() + salt));
        userRepo.save(user);
        return ResponseEntity.ok(user);
    }

    //#endregion
}
