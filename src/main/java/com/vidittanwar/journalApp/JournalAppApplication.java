package com.vidittanwar.journalApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class JournalAppApplication {

	public static void main(String[] args) {

		SpringApplication.run(JournalAppApplication.class, args);
	}


	@Bean
	public PlatformTransactionManager falana(MongoDatabaseFactory dbFactory){
		return new MongoTransactionManager(dbFactory);
	}

	@Bean
	public CommandLineRunner checkMongoUri(@Value("${spring.data.mongodb.uri:NOT_FOUND}") String uri) {
		return args -> {
			System.out.println("=== MONGO URI CHECK ===");
			System.out.println("Length: " + uri.length());
			System.out.println("Starts with: " + uri.substring(0, Math.min(20, uri.length())));
			System.out.println("=======================");
		};
	}
}