package com.freeejobs.jobListing.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freeejobs.jobListing.model.JobListing;
import com.freeejobs.jobListing.repository.JobListingRepository;

@Service
public class JobListingService {
	
	private static final Logger LOGGER = LogManager.getLogger(JobListingService.class);
	
	@Autowired
	private JobListingRepository jobListingRepository;
	
	public JobListing getJobListingById(long jobId) {
		return jobListingRepository.findById(jobId);
	}

	public List<JobListing> listJobListingByAuthorId(long authorId) {
		return jobListingRepository.findByAuthorId(authorId);
	}

}
