package com.freeejobs.jobListing.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.freeejobs.jobListing.constant.JobListingStatusEnum;
import com.freeejobs.jobListing.model.JobListing;
import com.freeejobs.jobListing.response.Status;

public class JobListingFixture {
	
	public static JobListing createJobListing() {

		JobListing jobListing = new JobListing();
        jobListing.setId(Long.valueOf(1));
        jobListing.setAuthorId(Long.valueOf(1));
        jobListing.setTitle("Test Title");
        jobListing.setRate("10");
        jobListing.setRateType("Per Hour");
        jobListing.setDetails("Test Details");
        jobListing.setDateCreated(new Date());
        jobListing.setDateUpdated(new Date());
        

        return jobListing;
    }
	
	public static List<JobListing> createJoblistingList() {

        List<JobListing> listings = new ArrayList<>();
        JobListing jobListing = new JobListing();
        jobListing.setId(Long.valueOf(1));
        jobListing.setAuthorId(Long.valueOf(1));
        jobListing.setTitle("Test Title");
        jobListing.setRate("10");
        jobListing.setRateType("Per Hour");
        jobListing.setDetails("Test Details");
        jobListing.setDateCreated(new Date());
        jobListing.setDateUpdated(new Date());
        listings.add(jobListing);

        return listings;
    }
	
	public static List<JobListing> createOpenJoblistingList() {

        List<JobListing> listings = new ArrayList<>();
        JobListing jobListing = new JobListing();
        jobListing.setId(Long.valueOf(2));
        jobListing.setAuthorId(Long.valueOf(2));
        jobListing.setStatus(JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode());
        jobListing.setTitle("Test Title");
        jobListing.setRate("10");
        jobListing.setRateType("Per Hour");
        jobListing.setDetails("Test Details");
        jobListing.setDateCreated(new Date());
        jobListing.setDateUpdated(new Date());
        listings.add(jobListing);

        return listings;
    }
	
	public static Optional<JobListing> createJobListingOptional() {

		JobListing listing = new JobListing();
        Optional<JobListing> listings = Optional.of(listing);
        listing.setId(Long.valueOf(2));
        listing.setAuthorId(Long.valueOf(2));
        listing.setStatus(JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode());
        listing.setTitle("Test Title");
        listing.setRate("10");
        listing.setRateType("Per Hour");
        listing.setDetails("Test Details");
        listing.setDateCreated(new Date());
        listing.setDateUpdated(new Date());

        return listings;
    }
	
//	public static Status createStatusError() {
//		Status status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed.");;
//		return status;
//	}
//	
//	public static Status createStatusOk() {
//		Status status = new Status(Status.Type.OK, "Successful.");
//		return status;
//	}
}
