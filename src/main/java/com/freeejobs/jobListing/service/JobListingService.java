package com.freeejobs.jobListing.service;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

	public List<JobListing> listAllOpenActiveJobListing(String status, String searchValue, Integer page, Integer numberOfListingPerPage) {
		return jobListingRepository.listAllOpenActiveJobListing(status, searchValue, PageRequest.of(page,numberOfListingPerPage));
	}

	public Integer getAllOpenActiveJobListingTotal(String status, String searchValue) {
		return jobListingRepository.getAllOpenActiveJobListingTotal(status, searchValue);
	}

	public JobListing addJobListing(JobListing jobListing) {
		jobListing.setDateCreated(new Date());
		jobListing.setDateUpdated(new Date());
		jobListing.setStatus("OFA");
		return jobListingRepository.save(jobListing);
		
	}

	public JobListing updateJobListing(JobListing jobListing) {
		JobListing oldJobListing = jobListingRepository.findById(jobListing.getId());
		jobListing.setDateCreated(oldJobListing.getDateCreated());
		jobListing.setDateUpdated(new Date());
		jobListing.setStatus(oldJobListing.getStatus());
		return jobListingRepository.save(jobListing);
		
	}

	public JobListing updateJobListingStatus(long id, String status) {
		JobListing oldJobListing = jobListingRepository.findById(id);
		oldJobListing.setDateUpdated(new Date());
		oldJobListing.setStatus(status);
		return jobListingRepository.save(oldJobListing);
		
	}

	public List<JobListing> listJobListingByAuthorIdAndStatus(long authorId, String status) {
		System.out.println("status:"+status);
		if (status.isEmpty()) {
			return jobListingRepository.findByAuthorId(authorId);
		}else {
			return jobListingRepository.findAllJobListingByAuthorIdAndStatus(authorId, status);
		}
		
	}

}
