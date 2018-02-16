package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties
@EnableCaching
public class BiddingSpringbootApplication {

	private static final Logger log = LoggerFactory.getLogger(BiddingSpringbootApplication.class);
	private final AtomicLong counter = new AtomicLong();
	
	
	public static void main(String[] args) {
		SpringApplication.run(BiddingSpringbootApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(EmployerRepository employerRepository,
			ProjectRepository projectRepository, BidRepository bidRepository) throws Exception {
		
		return args -> {
			log.info("\n\n Inside init of BiddingSpringbootApplication \t" );
			
			String pattern = "yyyy-MM-dd HH:mm";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	
			List<String> emplNames = new ArrayList<String>();
			emplNames.add("Employer1");
			emplNames.add("Employer2");
			String tmp = "2018-02-25 09:";String tmp1 = "2018-02-27 09:";
			long counterValue = counter.incrementAndGet();
			
			log.info("Rajeev debug\t" + tmp+String.valueOf(counterValue%60) + "\t" + "\n" + 
					simpleDateFormat.parse(tmp+String.valueOf(counterValue%60)) );
			
			for (String empName : emplNames) {
	
				Employer employer = employerRepository.save(new Employer(empName,
						"password"));
				Project proj1 = projectRepository.save(new Project(employer,
						"description" + counter.incrementAndGet() , simpleDateFormat.parse(tmp+String.valueOf(counterValue%60)), 
						counter.incrementAndGet()) );
				
				//bidRepository.save(new Bid(proj1, new Date(), counter.incrementAndGet()));
				bidRepository.save(new Bid(proj1, simpleDateFormat.parse(tmp1+String.valueOf(counterValue%60)), counter.incrementAndGet()));
				bidRepository.save(new Bid(proj1, new Date(), counter.incrementAndGet()));
				
				counterValue = counter.incrementAndGet();
				Project proj2 = projectRepository.save(new Project(employer,
						"description_a" + counter.incrementAndGet() ,  simpleDateFormat.parse(tmp+String.valueOf(counterValue%60)), 
						counter.incrementAndGet()) );
				
				bidRepository.save(new Bid(proj2, new Date(), counter.incrementAndGet()));
				
			}
			
			Collection<Employer> employers  = employerRepository.findAll();
			Collection<Project> projects = null;Collection<Bid> bids = null;
			for (Employer employer : employers) {
				log.info("Employer\t" + employer.getId() + "\t" + employer.getName());
				
				projects  = projectRepository.findByEmployerId(employer.getId());
				for (Project project : projects) {
					log.info("Project\t" + project.getId() + "\t" + project.getDescription() + "\t" + project.getBudgetAmount()
					     + "\t" + project.getProjEndDate());
					
					bids  = bidRepository.findByProjectId(project.getId());
					for (Bid bid : bids)
						log.info("Bid\t" + bid.getId() + "\t" + bid.getBidAmount() + "\t" + bid.getBidDate());
				}
			}
			
		};		
	}
	
}
