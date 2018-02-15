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
import java.util.Date;

@RestController
@RequestMapping("")
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

	@RequestMapping(method = RequestMethod.GET, value = "/employer/{empId}/project/{projectId}")
	Project getProject(@PathVariable long empId, @PathVariable long projectId) {
		this.validateUser(empId);
		return this.projectRepository.findOne(projectId);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/employer/{empId}")
	Collection<Project> getProjects(@PathVariable long empId) {
		this.validateUser(empId);
		return this.projectRepository.findByEmployerId(empId);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/projects")
	Collection<Project> getAllProjects() {
		return this.projectRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/employer/{empId}")
	ResponseEntity<?> add(@PathVariable long empId, @RequestBody Project input) {
		Employer employer = this.validateUser(empId);
	
		Project result = projectRepository.save(new Project(employer,
				input.getDescription(), input.getProjEndDate(), input.getBudgetAmount()));

		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest().path("/{id}")
			.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location).build();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/project/{projId}")
	ResponseEntity<?> bid(@PathVariable long projId, @RequestBody Bid input) {
	
		Project proj = this.validateProject(projId);
		
		Bid result = bidRepository.save(new Bid(proj, new Date(), input.getBidAmount()));

		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest().path("/{id}")
			.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	private Employer validateUser(long empId) {
		Employer employer = employerRepository.findOne(empId);
		if(  employer == null ) 
			throw new NotFoundException(empId);
		 
			return employer;
	}
	
	private Project validateProject(Long projId) {
		Project proj = projectRepository.findOne(projId);
		if(  proj == null ) 
			throw new NotFoundException(projId);
		 
		return proj;
	}
	
}
