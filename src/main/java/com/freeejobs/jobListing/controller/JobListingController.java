package com.freeejobs.jobListing.controller;

import java.io.Console;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

import com.freeejobs.jobListing.model.JobListing;
import com.freeejobs.jobListing.response.APIResponse;
import com.freeejobs.jobListing.response.Status;
import com.freeejobs.jobListing.service.JobListingService;

@RestController
@RequestMapping(value="/jobListing")
@CrossOrigin
public class JobListingController {
	
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
			jobListing = jobListingService.getJobListingById(listingId);
				if(jobListing == null) {
					status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to retrieve job listing.");
					//return null;
				} else {
					status = new Status(Status.Type.OK, "Successfully retrieve job listing.");
					
				}
				
				
			
		} catch (Exception e) {
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to retrieve job listing, Exception.");
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
			System.out.println(authorId);
			jobListings = jobListingService.listJobListingByAuthorId(authorId);
				if(jobListings == null) {
					System.out.println("null");
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list JobListing By AuthorId.");
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					status = new Status(Status.Type.OK, "Successfully list JobListing By AuthorId.");
				}
			
				
			
		} catch (Exception e) {
//			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list JobListing By AuthorId, Exception.");
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
		
		try {
			System.out.println(authorId);
			jobListings = jobListingService.listJobListingByAuthorIdAndStatus(authorId, status);
				if(jobListings == null) {
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list JobListing ByAuthorId And Status.");
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					responseStatus = new Status(Status.Type.OK, "Successfully list JobListing ByAuthorId And Status.");
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list JobListing ByAuthorId And Status, Exception.");
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
			String listingStatus = "OFA";
			jobListings = jobListingService.listAllOpenActiveJobListing(listingStatus, searchValue, ((int)pageNumber-1), numberOfListingPerPage);
				if(jobListings == null) {
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list All Open Active JobListing.");
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					status = new Status(Status.Type.OK, "Successfully list All Open Active JobListing.");
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to list All Open Active JobListing, Exception.");
		}
		resp.setData(jobListings);
		resp.setStatus(status);
		return resp;
	}
	@RequestMapping(value="/getAllOpenActiveJobListingTotal", method= RequestMethod.GET)
	public APIResponse getAllOpenActiveJobListingTotal(HttpServletResponse response, @RequestParam String searchValue) throws URISyntaxException {
		Integer jobListingsTotal = null;
		APIResponse resp = new APIResponse();
		Status status = new Status(Status.Type.OK, "Account login success.");
		
		try {
			String listingStatus = "OFA";
			jobListingsTotal = jobListingService.getAllOpenActiveJobListingTotal(listingStatus, searchValue);
				if(jobListingsTotal == null) {
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to get total of Open Active JobListing.");
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					status = new Status(Status.Type.OK, "Successfully get total of Open Active JobListing.");
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to get total of Open Active JobListing, Exception.");
		}
		resp.setData(jobListingsTotal);
		resp.setStatus(status);
		return resp;
		
	}
	
	@PostMapping("/create")
    public APIResponse createJobListing(HttpServletResponse response, @RequestBody JobListing jobListing) {
		//validate fields
		//handle errors
		APIResponse resp = new APIResponse();
		Status status = new Status(Status.Type.OK, "Account login success.");
		JobListing jobListingCreated = null;
		
		try {
			jobListingCreated = jobListingService.addJobListing(jobListing);
				if(jobListingCreated == null) {
					System.out.println("null");
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to create job listing.");
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					status = new Status(Status.Type.OK, "Successfully created job listing.");
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to create job listing, Exception.");
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
		
		try {
			jobListingUpdated = jobListingService.updateJobListing(jobListing);
				if(jobListingUpdated == null) {
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to update JobListing.");
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					status = new Status(Status.Type.OK, "Successfully get update JobListing.");
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			status = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to update JobListing, Exception.");
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
		
		try {
			jobListingUpdated = jobListingService.updateJobListingStatus(id,status);
				if(jobListingUpdated == null) {
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to update JobListing status.");
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					responseStatus = new Status(Status.Type.OK, "Successfully get update JobListing status.");
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to update JobListing status, Exception.");
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
			jobListing = jobListingService.getJobListingById(listingId);
				if(jobListing == null) {
					//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					//return null;
					responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to get Completed JobListing By Id.");
					
				} else {
					//response.setStatus(HttpServletResponse.SC_OK);
					responseStatus = new Status(Status.Type.OK, "Successfully get Completed JobListing By Id.");
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return null;
			responseStatus = new Status(Status.Type.INTERNAL_SERVER_ERROR, "Failed to get Completed JobListing By Id, Exception.");
		}
		resp.setData(jobListing);
		resp.setStatus(responseStatus);
		return resp;
	}

}
