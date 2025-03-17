package com.bookshelf.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookshelf.EntityDTONamespace.Book;

public interface BookRepo extends JpaRepository<Book, Long> {
}
