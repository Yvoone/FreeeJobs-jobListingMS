package com.freeejobs.jobListing.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.freeejobs.jobListing.model.JobListing;
import com.freeejobs.jobListing.model.JobListingAudit;

@Repository
public interface JobListingAuditRepository extends JpaRepository<JobListingAudit, Long> {
	

}
