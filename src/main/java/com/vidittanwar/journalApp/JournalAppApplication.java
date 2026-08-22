package com.vidittanwar.journalApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class JournalAppApplication {

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(JournalAppApplication.class);

		app.addListeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
			String uri = event.getEnvironment().getProperty("spring.data.mongodb.uri");
			System.out.println("=== SPRING ENV CHECK ===");
			if (uri == null) {
				System.out.println("spring.data.mongodb.uri => NULL");
			} else {
				System.out.println("Length: " + uri.length());
				System.out.println("Starts with: " + uri.substring(0, Math.min(20, uri.length())));
			}
			System.out.println("=========================");
		});

		app.run(args);
	}


	@Bean
	public PlatformTransactionManager falana(MongoDatabaseFactory dbFactory){
		return new MongoTransactionManager(dbFactory);
	}
}