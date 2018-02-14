package com.example;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;

@Entity
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Employer employer;
    
    @OneToMany(mappedBy = "project")
    private Set<Bid> bids = new HashSet<>();

    private long budgetAmount;

	@Temporal(TemporalType.DATE)
    private Date projEndDate;

    private String description;

    private Project() { } // JPA only

    public Project(final Employer employer, final String description, final Date projEndDate, final long budgetAmount ) {
        this.description = description;
        this.projEndDate = projEndDate;
        this.employer = employer;
        this.budgetAmount = budgetAmount;
    }

    public Long getId() {
        return id;
    }

    public Employer getEmployer() {
        return employer;
    }

    public String getDescription() {
        return description;
    }
    
    public long getBudgetAmount() {
		return budgetAmount;
	}
    
    public Date getProjecteEndDate() {
		return projEndDate;
	}

	public Set<Bid> getBids() {
		return bids;
	}

}
