package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/{userId}/bookmarks")
class BidRestController {

	private final EmployerRepository employerRepository;
	private final BidRepository bidRepository;
	private final ProjectRepository projectRepository;

	@Autowired
	BidRestController(EmployerRepository employerRepository,
			ProjectRepository projectRepository, BidRepository bidRepository) {
		this.employerRepository = employerRepository;
		this.projectRepository = projectRepository;
		this.bidRepository = bidRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	Collection<Project> getProjects(@PathVariable String empName) {
		this.validateUser(empName);
		return this.projectRepository.findByEmployerName(empName);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	Collection<Project> getProjects() {
		return this.projectRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> add(@PathVariable String empName, @RequestBody Project input) {
		Employer employer = this.validateUser(empName);
	
		Project result = projectRepository.save(new Project(employer,
				input.getDescription(), input.getProjecteEndDate(), input.getBudgetAmount()));

		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest().path("/{id}")
			.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location).build();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> bid(@PathVariable long projId, @RequestBody Bid input) {
	
		Project proj = this.validateProject(projId);
		
		Bid result = bidRepository.save(new Bid(proj, input.getBidDate(), input.getBidAmount()));

		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest().path("/{id}")
			.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{projectid}")
	Project readProject(@PathVariable String empName, @PathVariable Long projectId) {
		this.validateUser(empName);
		return this.projectRepository.findOne(projectId);
	}

	private Employer validateUser(String empName) throws NotFoundException {
		Employer employer = employerRepository.findByName(empName);
		if(  employer == null ) 
			new NotFoundException(empName);
		 
			return employer;
	}
	
	private Project validateProject(Long projId) throws NotFoundException {
		Project proj = projectRepository.findOne(projId);
		if(  proj == null ) 
			new NotFoundException(projId);
		 
		return proj;
	}
	
}
