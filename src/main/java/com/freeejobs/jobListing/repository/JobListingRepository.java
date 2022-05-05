package com.freeejobs.jobListing.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.freeejobs.jobListing.dto.JobListingDTO;
import com.freeejobs.jobListing.model.JobListing;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, Long> {
	public JobListing save(JobListingDTO listing);
	public JobListing findById(long id);
	public List<JobListing> findAll();
	
	@Query("select t from JobListing t where t.authorId = ?1")
	public List<JobListing> findByAuthorId(long authorId);
	
	@Query(value = "select t from JobListing t where t.status = ?1 and lower(t.title) like concat('%', lower(?2), '%')"
			, countQuery = "select COUNT(t) from JobListing t where t.status = ?1 and lower(t.title) like concat('%', lower(?2), '%')")
	public List<JobListing> listAllOpenActiveJobListing(String status, String searchValue, Pageable pageable);
	@Query("select COUNT(t) from JobListing t where t.status = ?1 and lower(t.title) like concat('%', lower(?2), '%')")
	public Integer getAllOpenActiveJobListingTotal(String status, String searchValue);
	
	
	public List<JobListing> findAllJobListingByAuthorIdAndStatus(long authorId, String status);

}
