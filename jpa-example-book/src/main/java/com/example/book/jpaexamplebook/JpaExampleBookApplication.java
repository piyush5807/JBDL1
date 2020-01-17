package com.example.book.jpaexamplebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.tools.jar.CommandLine;

@SpringBootApplication
public class JpaExampleBookApplication implements CommandLineRunner {

	@Autowired
	private BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(JpaExampleBookApplication.class, args);
	}

	@Override
	public void run(String...data){
		bookRepository.save(new Book( "Intro to C++", "A", 100));
		bookRepository.save(new Book( "Intro to C++", "B", 140));

		bookRepository.save(new Book("Intro To java", "A", 123));

		System.out.println(bookRepository.findById(3));
		System.out.println(bookRepository.findByAuthorName("B"));

	}

}
