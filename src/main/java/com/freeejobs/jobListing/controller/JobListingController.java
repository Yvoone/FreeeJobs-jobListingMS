package com.freeejobs.jobListing.controller;

import java.io.Console;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.freeejobs.jobListing.constant.JobListingStatusEnum;
import com.freeejobs.jobListing.model.JobListing;
import com.freeejobs.jobListing.response.APIResponse;
import com.freeejobs.jobListing.response.Status;
import com.freeejobs.jobListing.service.JobListingService;

@RestController
@RequestMapping(value="/jobListing")
@CrossOrigin
public class JobListingController {
	
	private static Logger LOGGER = LogManager.getLogger(JobListingController.class);
	
	@Autowired
	private JobListingService jobListingService;
	
	private Integer numberOfListingPerPage = 10;
	
	@RequestMapping(value="/getJobListing", method= RequestMethod.GET)
	public APIResponse getJobListingById(HttpServletResponse response,
			@RequestParam long listingId) throws URISyntaxException {
		APIResponse resp = new APIResponse();
		Status status = new Status(Status.Type.OK, "Account login success.");
		JobListing jobListing = null;
		
		try {
			if(!jobListingService.isId(String.valueOf(listingId))){
				status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to retrieve job listing. Invalid listing Id.");
				LOGGER.error(status.toString());
			}else {
				jobListing = jobListingService.getJobListingById(listingId);
				if(jobListing == null) {
					status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to retrieve job listing.");
					LOGGER.error(status.toString());
					//return null;
				} else {
					status = new Status(Status.Type.OK, "Successfully retrieve job listing.");
					
				}
			}
			
				
				
			
		} catch (Exception e) {
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to retrieve job listing, Exception.");
			LOGGER.error(e.getMessage(), e);
			//return null;
		}
		resp.setData(jobListing);
		resp.setStatus(status);
		return resp;
	}
	
	@RequestMapping(value="/listJobListingByAuthorId", method= RequestMethod.GET)
	public APIResponse listJobListingByAuthorId(HttpServletResponse response,
			@RequestParam long authorId) throws URISyntaxException {
		APIResponse resp = new APIResponse();
		Status status = new Status(Status.Type.OK, "Account login success.");
		List<JobListing> jobListings = null;
		
		try {
			if(!jobListingService.isId(String.valueOf(authorId))){
				status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list JobListing By AuthorId. Invalid author Id.");
				LOGGER.error(status.toString());
			}else {
				System.out.println(authorId);
				jobListings = jobListingService.listJobListingByAuthorId(authorId);
					if(jobListings == null) {
						System.out.println("null");
						//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						//return null;
						status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list JobListing By AuthorId.");
						LOGGER.error(status.toString());
						
					} else {
						//response.setStatus(HttpServletResponse.SC_OK);
						status = new Status(Status.Type.OK, "Successfully list JobListing By AuthorId.");
					}
			}				
			
		} catch (Exception e) {
//			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list JobListing By AuthorId, Exception.");
			LOGGER.error(e.getMessage(), e);
		}
		resp.setData(jobListings);
		resp.setStatus(status);
		return resp;
	}
	
	@RequestMapping(value="/listJobListingByAuthorIdAndStatus", method= RequestMethod.GET)
	public APIResponse listJobListingByAuthorIdAndStatus(HttpServletResponse response,
			@RequestParam long authorId, @RequestParam String status) throws URISyntaxException {
		
		List<JobListing> jobListings = null;
		APIResponse resp = new APIResponse();
		Status responseStatus = new Status(Status.Type.OK, "Account login success.");
		List<String> errors = new ArrayList<String>();
		
		try {
			if(jobListingService.isStatusInvalid(status)) {
				errors.add("Invalid status value");
			}
			if(!jobListingService.isId(String.valueOf(authorId))){
				errors.add("Invalid author id value");
			}
			if(errors.isEmpty()) {
				System.out.println(authorId);
				jobListings = jobListingService.listJobListingByAuthorIdAndStatus(authorId, status);
					if(jobListings == null) {
						//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						//return null;
						responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list JobListing ByAuthorId And Status.");
						LOGGER.error(status.toString());
						
					} else {
						//response.setStatus(HttpServletResponse.SC_OK);
						responseStatus = new Status(Status.Type.OK, "Successfully list JobListing ByAuthorId And Status.");
					}
			}else {
				responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list JobListing ByAuthorId And Status. Invalid status or id.");
				String listOfErrors = errors.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));
				LOGGER.error(status.toString()+" "+listOfErrors);
			}
		} 
		catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list JobListing ByAuthorId And Status, Exception.");
			LOGGER.error(e.getMessage(), e);
		}
		resp.setData(jobListings);
		resp.setStatus(responseStatus);
		return resp;
	}
	
	@RequestMapping(value="/listAllOpenActiveJobListing", method= RequestMethod.GET)
	public APIResponse listAllOpenActiveJobListing(HttpServletResponse response, 
			@RequestParam long pageNumber, @RequestParam String searchValue) throws URISyntaxException {
		
		List<JobListing> jobListings = null;
		APIResponse resp = new APIResponse();
		Status status = new Status(Status.Type.OK, "Account login success.");
		
		try {
			String listingStatus = JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode();
			jobListings = jobListingService.listAllOpenActiveJobListing(listingStatus, searchValue, ((int)pageNumber-1), numberOfListingPerPage);
				if(jobListings == null) {
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list All Open Active JobListing.");
					LOGGER.error(status.toString());
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					status = new Status(Status.Type.OK, "Successfully list All Open Active JobListing.");
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list All Open Active JobListing, Exception.");
			LOGGER.error(e.getMessage(), e);
		}
		resp.setData(jobListings);
		resp.setStatus(status);
		return resp;
	}
	
	@RequestMapping(value="/getAllOpenActiveJobListingTotal", method= RequestMethod.GET)
	public APIResponse getAllOpenActiveJobListingTotal(HttpServletResponse response) throws URISyntaxException {
		Integer jobListingsTotal = null;
		APIResponse resp = new APIResponse();
		Status status = new Status(Status.Type.OK, "Account login success.");
		
		try {
			String listingStatus = JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode();
			jobListingsTotal = jobListingService.getAllOpenActiveJobListingTotal(listingStatus, "");
				if(jobListingsTotal == null) {
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to get total of Open Active JobListing.");
					LOGGER.error(status.toString());
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					status = new Status(Status.Type.OK, "Successfully get total of Open Active JobListing.");
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to get total of Open Active JobListing, Exception.");
			LOGGER.error(e.getMessage(), e);
		}
		resp.setData(jobListingsTotal);
		resp.setStatus(status);
		return resp;
		
	}
	/* 
	@RequestMapping(value="/getAllOpenActiveJobListingTotal", method= RequestMethod.GET)
	public APIResponse getAllOpenActiveJobListingTotal(HttpServletResponse response, @RequestParam String searchValue) throws URISyntaxException {
		Integer jobListingsTotal = null;
		APIResponse resp = new APIResponse();
		Status status = new Status(Status.Type.OK, "Account login success.");
		
		try {
			String listingStatus = JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode();
			jobListingsTotal = jobListingService.getAllOpenActiveJobListingTotal(listingStatus, searchValue);
				if(jobListingsTotal == null) {
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to get total of Open Active JobListing.");
					LOGGER.error(status.toString());
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					status = new Status(Status.Type.OK, "Successfully get total of Open Active JobListing.");
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to get total of Open Active JobListing, Exception.");
			LOGGER.error(e.getMessage(), e);
		}
		resp.setData(jobListingsTotal);
		resp.setStatus(status);
		return resp;
		
	} */
	
	@PostMapping("/create")
    public APIResponse createJobListing(HttpServletResponse response, @RequestBody JobListing jobListing) {
		//validate fields
		//handle errors
		APIResponse resp = new APIResponse();
		Status status = new Status(Status.Type.OK, "Account login success.");
		JobListing jobListingCreated = null;
		List<String> errors = new ArrayList<String>();
		
		try {
			if(jobListingService.isBlank(jobListing.getTitle())) {
				errors.add("Invalid title value");
			}
			if(jobListingService.isBlank(jobListing.getDetails())) {
				errors.add("Invalid details value");
			}
			if(jobListingService.isBlank(jobListing.getRate())) {
				errors.add("Invalid details value");
			}
			if(jobListingService.isBlank(jobListing.getRateType())) {
				errors.add("Invalid details value");
			}
			if(!jobListingService.isId(String.valueOf(jobListing.getAuthorId()))) {
				errors.add("Invalid author id value");
			}
			if(errors.isEmpty()) {
				jobListingCreated = jobListingService.addJobListing(jobListing);
				if(jobListingCreated == null) {
					System.out.println("null");
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to create job listing.");
					LOGGER.error(status.toString());
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					status = new Status(Status.Type.OK, "Successfully created job listing.");
				}
			}else {
				status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to create job listing. Invalid JobListing Object.");
				String listOfErrors = errors.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));
				LOGGER.error(status.toString()+" "+listOfErrors);
			}
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to create job listing, Exception.");
			LOGGER.error(e.getMessage(), e);
		}
		resp.setData(jobListingCreated);
		resp.setStatus(status);
		return resp;
//		jobListingService.addJobListing(jobListing);
//		return null;
    }

    @PutMapping("/{id}/edit")
    public APIResponse updateJobListing(@PathVariable("id") Long id, @RequestBody JobListing jobListing) {
    	JobListing jobListingUpdated = null;
    	APIResponse resp = new APIResponse();
		Status status = new Status(Status.Type.OK, "Account login success.");
		List<String> errors = new ArrayList<String>();
		
		try {
			if(jobListingService.isBlank(jobListing.getTitle())) {
				errors.add("Invalid title value");
			}
			if(jobListingService.isBlank(jobListing.getDetails())) {
				errors.add("Invalid details value");
			}
			if(jobListingService.isBlank(jobListing.getRate())) {
				errors.add("Invalid details value");
			}
			if(jobListingService.isBlank(jobListing.getRateType())) {
				errors.add("Invalid details value");
			}
			if(!jobListingService.isId(String.valueOf(jobListing.getAuthorId()))) {
				errors.add("Invalid author id value");
			}
			if(errors.isEmpty()) {
				jobListingUpdated = jobListingService.updateJobListing(jobListing);
				if(jobListingUpdated == null) {
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to update JobListing.");
					LOGGER.error(status.toString());
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					status = new Status(Status.Type.OK, "Successfully get update JobListing.");
				}
			}else {
				status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to create job listing. Invalid JobListing Object.");
				String listOfErrors = errors.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));
				LOGGER.error(status.toString()+" "+listOfErrors);
			}
			
			
				
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to update JobListing, Exception.");
			LOGGER.error(e.getMessage(), e);
		}
		resp.setData(jobListingUpdated);
		resp.setStatus(status);
		return resp;
    }
    
    @PutMapping("/{id}/updateJobListingStatus")
    public APIResponse updateJobListingStatus(@PathVariable("id") Long id, @RequestBody String status) {
    	System.out.println(id);
    	System.out.println(status);
    	JobListing jobListingUpdated = null;
    	APIResponse resp = new APIResponse();
		Status responseStatus = new Status(Status.Type.OK, "Account login success.");
		List<String> errors = new ArrayList<String>();
		
		try {
			if(jobListingService.isStatusInvalid(status)) {
				errors.add("Invalid status value");
			}
			if(!jobListingService.isId(String.valueOf(id))){
				errors.add("Invalid id value");
			}
			if(errors.isEmpty()) {
				jobListingUpdated = jobListingService.updateJobListingStatus(id,status);
				if(jobListingUpdated == null) {
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to update JobListing status.");
					LOGGER.error(status.toString());
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					responseStatus = new Status(Status.Type.OK, "Successfully get update JobListing status.");
				}
			}else {
				responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to create job listing. Invalid JobListing Object.");
				String listOfErrors = errors.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));
				LOGGER.error(status.toString()+" "+listOfErrors);
			}
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to update JobListing status, Exception.");
			LOGGER.error(e.getMessage(), e);
		}
		resp.setData(jobListingUpdated);
		resp.setStatus(responseStatus);
		return resp;
		
    }
	
    @RequestMapping(value="/getCompletedJobListing", method= RequestMethod.GET)
	public APIResponse getCompletedJobListingById(HttpServletResponse response,
			@RequestParam long listingId) throws URISyntaxException {
		
		JobListing jobListing = null;
		APIResponse resp = new APIResponse();
		Status responseStatus = new Status(Status.Type.OK, "Account login success.");
		
		try {
			if(!jobListingService.isId(String.valueOf(listingId))){
				responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to get Completed JobListing By Id. Invalid Id.");
				LOGGER.error(responseStatus.toString());
			}else {
				jobListing = jobListingService.getJobListingById(listingId);
				if(jobListing == null) {
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to get Completed JobListing By Id.");
					LOGGER.error(responseStatus.toString());
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					responseStatus = new Status(Status.Type.OK, "Successfully get Completed JobListing By Id.");
				}
			}	
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to get Completed JobListing By Id, Exception.");
			LOGGER.error(e.getMessage(), e);
		}
		resp.setData(jobListing);
		resp.setStatus(responseStatus);
		return resp;
	}

}
