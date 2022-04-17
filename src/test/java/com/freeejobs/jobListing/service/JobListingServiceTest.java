package com.freeejobs.jobListing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.freeejobs.jobListing.WebConfig;
import com.freeejobs.jobListing.controller.JobListingController;
import com.freeejobs.jobListing.controller.JobListingFixture;
import com.freeejobs.jobListing.model.JobListing;
import com.freeejobs.jobListing.repository.JobListingAuditRepository;
import com.freeejobs.jobListing.repository.JobListingRepository;
import com.freeejobs.jobListing.response.APIResponse;

@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class JobListingServiceTest {
	@Mock
	private JobListingRepository jobListingRepository;
	
	@Mock
	private JobListingAuditRepository jobListingAuditRepository;
	
	@InjectMocks
    private JobListingService JobListingService;
	
	private JobListing jobListing;
	private List<JobListing> jobListings;
	private List<JobListing> jobListingsOpenStatus;
	private Optional<JobListing> jobListingOptional;
	private int numberOfListingPerPage=10;
	
	@BeforeEach
    void setUp() {
        jobListing = JobListingFixture.createJobListing();
        jobListings = JobListingFixture.createJoblistingList();
        jobListingsOpenStatus = JobListingFixture.createOpenJoblistingList();
        jobListingOptional = JobListingFixture.createJobListingOptional();
    }
	
//	@Test
//    void testGetJobListingById() throws URISyntaxException {    
//        HttpServletResponse response = mock(HttpServletResponse.class); 
//        Long jobId = Long.valueOf(1);
//        JobListingRepository repo = mock(JobListingRepository.class);
//        when(jobListingRepository.findById(jobId)).thenReturn(jobListing);
//
//        JobListing listings = JobListingService.getJobListingById(jobId);
//        assertEquals(jobListing.getId(), listings.getId());
//    }

}
