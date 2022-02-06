package com.freeejobs.jobListing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.freeejobs.jobListing.model.JobListing;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, Long> {
	public JobListing findById(long id);
	public List<JobListing> findAll();
	
	@Query("select t from JobListing t where t.authorId = ?1")
	public List<JobListing> findByAuthorId(long authorId);

}
