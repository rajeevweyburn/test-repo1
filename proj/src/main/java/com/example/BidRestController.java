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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
		Project project = this.projectRepository.findOne(projectId);
		
		SortedSet<Bid> mybids = new TreeSet<>(new Comparator<Bid>() {
	        public int compare(Bid b1, Bid b2) {
	            if( b1.getBidAmount() > b2.getBidAmount()) return 1;
	            else if ( b1.getBidAmount() < b2.getBidAmount()) return -1;
	            else return 0;
	        }
	    });
		
		mybids.addAll(project.getBids());
		Bid leastBid = null;
		
		Iterator<Bid> it = mybids.iterator();
	     while(it.hasNext()){
	    	 leastBid = it.next();
	        if( leastBid.getBidDate().before(project.getProjEndDate()) )
	        	break;
	     }
	     
		Set<Bid> bids = new HashSet<Bid>();
		if( leastBid != null ) {
			bids.add(leastBid);
			project.setBids(bids);
		}
		
		return project;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/employer/{empId}")
	Collection<Project> getProjects(@PathVariable long empId) {
		this.validateUser(empId);
		return this.projectRepository.findByEmployerId(empId);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/projects")
	Collection<Project> getAllProjects() {
		
		Collection<Project> projects = this.projectRepository.findAll();
		
		Collection<Project> notExpiredProjects = new ArrayList<Project>();
		
		 for(Project proj : projects) {
			 if( proj.getProjEndDate().after(new Date()))
				 notExpiredProjects.add(proj);
		 }
		
		return notExpiredProjects;
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
		
		if(  new Date().after(proj.getProjEndDate()))
			throw new NotFoundException(projId);
			
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
