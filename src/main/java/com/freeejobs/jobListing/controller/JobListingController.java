package com.freeejobs.jobListing.controller;

import java.io.Console;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.freeejobs.jobListing.model.JobListing;
import com.freeejobs.jobListing.service.JobListingService;

@RestController
@RequestMapping(value="/jobListing")
@CrossOrigin
public class JobListingController {
	
	@Autowired
	private JobListingService jobListingService;
	
	private Integer numberOfListingPerPage = 10;
	
	@RequestMapping(value="/getJobListing", method= RequestMethod.GET)
	public JobListing getJobListingById(HttpServletResponse response,
			@RequestParam long listingId) throws URISyntaxException {
		
		JobListing jobListing = null;
		
		try {
			jobListing = jobListingService.getJobListingById(listingId);
				if(jobListing == null) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					return null;
				} else {
					response.setStatus(HttpServletResponse.SC_OK);
				}
			
				
			
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		return jobListing;
	}
	
	@RequestMapping(value="/listJobListingByAuthorId", method= RequestMethod.GET)
	public List<JobListing> listJobListingByAuthorId(HttpServletResponse response,
			@RequestParam long authorId) throws URISyntaxException {
		
		List<JobListing> jobListings = null;
		
		try {
			System.out.println(authorId);
			jobListings = jobListingService.listJobListingByAuthorId(authorId);
				if(jobListings == null) {
					System.out.println("null");
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					return null;
				} else {
					response.setStatus(HttpServletResponse.SC_OK);
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		return jobListings;
	}
	
	@RequestMapping(value="/listAllOpenActiveJobListing", method= RequestMethod.GET)
	public List<JobListing> listAllOpenActiveJobListing(HttpServletResponse response, 
			@RequestParam long pageNumber, @RequestParam String searchValue) throws URISyntaxException {
		
		List<JobListing> jobListings = null;
		
		try {
			String status = "Pending";
			jobListings = jobListingService.listAllOpenActiveJobListing(status, searchValue, ((int)pageNumber-1), numberOfListingPerPage);
				if(jobListings == null) {
					System.out.println("null");
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					return null;
				} else {
					response.setStatus(HttpServletResponse.SC_OK);
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		return jobListings;
	}
	@RequestMapping(value="/getAllOpenActiveJobListingTotal", method= RequestMethod.GET)
	public Integer getAllOpenActiveJobListingTotal(HttpServletResponse response, @RequestParam String searchValue) throws URISyntaxException {
		
		Integer jobListingsTotal = null;
		
		try {
			String status = "Pending";
			jobListingsTotal = jobListingService.getAllOpenActiveJobListingTotal(status, searchValue);
				if(jobListingsTotal == null) {
					System.out.println("null");
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					return null;
				} else {
					response.setStatus(HttpServletResponse.SC_OK);
				}
			
				
			
		} catch (Exception e) {
			System.out.println(e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		return jobListingsTotal;
	}
	

}
