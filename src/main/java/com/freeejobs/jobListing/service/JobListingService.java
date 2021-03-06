package com.freeejobs.jobListing.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.freeejobs.jobListing.constant.AuditEnum;
import com.freeejobs.jobListing.constant.JobListingStatusEnum;
import com.freeejobs.jobListing.dto.JobListingDTO;
import com.freeejobs.jobListing.model.JobListing;
import com.freeejobs.jobListing.model.JobListingAudit;
import com.freeejobs.jobListing.repository.JobListingAuditRepository;
import com.freeejobs.jobListing.repository.JobListingRepository;

@Service
public class JobListingService {
	
	private static final Logger LOGGER = LogManager.getLogger(JobListingService.class);
	
	@Autowired
	private JobListingRepository jobListingRepository;
	
	@Autowired
	private JobListingAuditRepository jobListingAuditRepository;
	
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

	public JobListing addJobListing(JobListingDTO jobListing) {
		JobListing newJobListing = new JobListing();
		newJobListing.setTitle(jobListing.getTitle());
		newJobListing.setAuthorId(jobListing.getAuthorId());
		newJobListing.setDetails(jobListing.getDetails());
		newJobListing.setRate(jobListing.getRate());
		newJobListing.setRateType(jobListing.getRateType());
		newJobListing.setDateCreated(new Date());
		newJobListing.setDateUpdated(new Date());
		newJobListing.setStatus(JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode());
		
		JobListing addedListing = jobListingRepository.save(newJobListing);
		insertAudit(addedListing, AuditEnum.INSERT.getCode());
		
		return addedListing;
		
	}

	public JobListing updateJobListing(JobListingDTO jobListing) {
		JobListing oldJobListing = jobListingRepository.findById(jobListing.getId());
		oldJobListing.setTitle(jobListing.getTitle());
		oldJobListing.setAuthorId(jobListing.getAuthorId());
		oldJobListing.setDetails(jobListing.getDetails());
		oldJobListing.setRate(jobListing.getRate());
		oldJobListing.setRateType(jobListing.getRateType());
		JobListing updatedListing = jobListingRepository.save(oldJobListing);
		insertAudit(updatedListing ,AuditEnum.UPDATE.getCode());
		insertAudit(updatedListing, AuditEnum.UPDATE.getCode());
		
		return updatedListing;
		
	}

	public JobListing updateJobListingStatus(long id, String status) {
		JobListing oldJobListing = jobListingRepository.findById(id);
		oldJobListing.setDateUpdated(new Date());
		oldJobListing.setStatus(status);
		JobListing updatedListing = jobListingRepository.save(oldJobListing);
		insertAudit(updatedListing, AuditEnum.UPDATE.getCode());
		
		return updatedListing;
		
	}

	public List<JobListing> listJobListingByAuthorIdAndStatus(long authorId, String status) {
		System.out.println("status:"+status);
		if (status.isEmpty()) {
			return jobListingRepository.findByAuthorId(authorId);
		}else {
			return jobListingRepository.findAllJobListingByAuthorIdAndStatus(authorId, status);
		}
		
	}
	public boolean isId(String id) {
		return String.valueOf(id).matches("[0-9]+");
	}
	
	public boolean isStatusInvalid(String status) {
		return !StringUtils.isEmpty(status)&&!JobListingStatusEnum.Constants.JOB_LISTING_STATUS_LIST.contains(status);
	}
	
	public boolean isBlank(String value) {
		return StringUtils.isBlank(value);
	}

	public JobListingAudit insertAudit(JobListing jobListing, String opsType) {
		JobListingAudit newAuditEntry = new JobListingAudit();
		newAuditEntry.setAuditData(jobListing.toString());
		newAuditEntry.setOpsType(opsType);
		newAuditEntry.setDateCreated(new Date());
		newAuditEntry.setCreatedBy(String.valueOf(jobListing.getAuthorId()));
		
		return jobListingAuditRepository.save(newAuditEntry);
	}
	
//	public String jobListingToString(JobListing listing) {
//		String listingString = "";
//		if(listing.getTitle()!=null)
//		{
//			listingString.concat("title: "+listing.getTitle()+" | ");
//		}
//		if(listing.getDetails()!=null) {
//			listingString.concat("details: "+listing.getDetails()+" | ");
//		}
//		if(listing.getDetails()!=null) {
//			listingString.concat("author: "+listing.getAuthorId()+" | ");
//		}
//		if(listing.getDetails()!=null) {
//			listingString.concat("rate: "+listing.getRate()+" | ");
//		}
//		if(listing.getDetails()!=null) {
//			listingString.concat("rateType: "+listing.getRateType()+" | ");
//		}
//		if(listing.getDetails()!=null) {
//			listingString.concat("status: "+listing.getStatus());
//		}
//		return listingString;
//		
//		
//	}

}
