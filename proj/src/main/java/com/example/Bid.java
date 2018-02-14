package com.example;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;

@Entity
public class Bid {

    @Id
    @GeneratedValue
    private Long id;

    private long bidAmount;

	@Temporal(TemporalType.DATE)
    private Date bidDate;
	
	@JsonIgnore
    @ManyToOne
    private Project project;

    private Bid() { }

    public Bid(final Project project, final Date bidDate, final long bidAmount ) {
        this.bidDate = bidDate;
        this.project = project;
        this.bidAmount = bidAmount;
    }

    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }
  
    public long getBidAmount() {
		return bidAmount;
	}
    
    public Date getBidDate() {
		return bidDate;
	}
    
}

