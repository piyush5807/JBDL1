package com.example.book.jpaexamplebook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Integer> {

}
