package com.healthcare.analytics.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.springframework.boot.SpringApplication.*;

/**
 * Main application class for Healthcare Analytics Dashboard
 * This is a Spring Boot application that provides analytics and dashboard functionality
 * for healthcare data management including patients, doctors, visits, and prescriptions.
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
public class DashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardApplication.class,args);;
		//run(DashboardApplication.class, args);
		System.out.println("Healthcare Analytics Dashboard Application started successfully!");
	}
}
