package paf.day27.d27workshop;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.day27.d27workshop.repo.ReviewRepo;

@SpringBootApplication
public class D27workshopApplication{
	// implements CommandLineRunner

	@Autowired
	ReviewRepo repo;

	public static void main(String[] args) {
		SpringApplication.run(D27workshopApplication.class, args);
	}

	// @Override
	// public void run(String... args) throws Exception {
	// 	Document d = repo.getReviewByID("yscI8137");
	// 	repo.isReviewEdited(d);
	// }

}
