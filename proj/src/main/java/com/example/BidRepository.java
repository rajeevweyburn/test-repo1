package com.example;


import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BidRepository extends JpaRepository<Bid, Long> {
	
	Collection<Bid> findByProjectId(Long id);

}
