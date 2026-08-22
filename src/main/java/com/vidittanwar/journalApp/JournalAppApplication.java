package com.vidittanwar.journalApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class JournalAppApplication {

	public static void main(String[] args) {

		String uri = System.getenv("SPRING_DATA_MONGODB_URI");
		System.out.println("=== ENV CHECK ===");
		System.out.println("Value is null? " + (uri == null));
		if (uri != null) {
			System.out.println("Length: " + uri.length());
			System.out.println("Starts with: " + uri.substring(0, Math.min(20, uri.length())));
		}
		System.out.println("=================");

		SpringApplication.run(JournalAppApplication.class, args);
	}


	@Bean
	public PlatformTransactionManager falana(MongoDatabaseFactory dbFactory){
		return new MongoTransactionManager(dbFactory);
	}
}