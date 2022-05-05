package com.freeejobs.jobListing.controller;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.context.WebApplicationContext;

import com.freeejobs.jobListing.WebConfig;
import com.freeejobs.jobListing.constant.JobListingStatusEnum;
import com.freeejobs.jobListing.dto.JobListingDTO;
import com.freeejobs.jobListing.model.JobListing;
import com.freeejobs.jobListing.response.APIResponse;
import com.freeejobs.jobListing.response.Status;
import com.freeejobs.jobListing.service.JobListingService;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class JobListingControllerTest {
	
	@Mock
	private JobListingService jobListingService;
	
	@InjectMocks
    private JobListingController jobListingController;
	
	@Captor
    private ArgumentCaptor<JobListing> jobListingArgumentCaptor;

	
	private JobListing jobListing;
	private JobListingDTO jobListingDTO;
	private List<JobListing> jobListings;
	private List<JobListing> jobListingsOpenStatus;
	private int numberOfListingPerPage=10;
	
	@BeforeEach
    void setUp() {
        jobListing = JobListingFixture.createJobListing();
        jobListingDTO = JobListingFixture.createJobListingDTO();
        jobListings = JobListingFixture.createJoblistingList();
        jobListingsOpenStatus = JobListingFixture.createOpenJoblistingList();
    }
	
	//getJobListingById
	
	@Test
    void testGetJobListingByIdIsID() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        Long id = Long.valueOf(1);
        when(jobListingService.isId(String.valueOf(id))).thenReturn(true);
        when(jobListingService.getJobListingById(id)).thenReturn(jobListing);

        APIResponse listings = jobListingController.getJobListingById(response, jobListing.getId());
        assertEquals(jobListing.getId(), ((JobListing) listings.getData()).getId());
    }
	
	@Test
    void testGetJobListingByIdisNotID() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        Long id = Long.valueOf(1);
        when(jobListingService.isId(String.valueOf(id))).thenReturn(false);
        //when(jobListingService.getJobListingById(id)).thenReturn(null);

        APIResponse listings = jobListingController.getJobListingById(response, jobListing.getId());
        assertEquals(((JobListing) listings.getData()), null);
        //assertEquals(jobListing.getId(), ((JobListing) listings.getData()).getId());
    }
	
	@Test
    void testGetJobListingByIdNull() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        Long id = Long.valueOf(1);
        when(jobListingService.isId(String.valueOf(id))).thenReturn(true);
        when(jobListingService.getJobListingById(id)).thenReturn(null);

        APIResponse listings = jobListingController.getJobListingById(response, jobListing.getId());
        assertEquals(((JobListing) listings.getData()), null);
        //assertNull((JobListing) listings.getData());
        //assertEquals(jobListing.getId(), ((JobListing) listings.getData()).getId());
    }
	
	@Test
    void testGetJobListingByIdThrowException() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        Long id = Long.valueOf(1);
        when(jobListingService.isId(String.valueOf(id))).thenReturn(true);
        when(jobListingService.getJobListingById(id)).thenThrow(UnexpectedRollbackException.class);
//        assertThrows(UnexpectedRollbackException.class,
//                ()->{jobListingController.getJobListingById(response, jobListing.getId());
//                });
        APIResponse listings = jobListingController.getJobListingById(response, jobListing.getId());
        //assertTrue(listings.getMessage().contains("Stuff"));
        assertNull((JobListing) listings.getData());
        //assertEquals(jobListing.getId(), ((JobListing) listings.getData()).getId());
    }
	
	//listJobListingByAuthorId
	
	@SuppressWarnings("unchecked")
	@Test
    void testListJobListingByAuthorIdIsID() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        Long authorId = Long.valueOf(1);
        when(jobListingService.isId(String.valueOf(authorId))).thenReturn(true);
        when(jobListingService.listJobListingByAuthorId(authorId)).thenReturn(jobListings);

        APIResponse listings = jobListingController.listJobListingByAuthorId(response, jobListing.getAuthorId());
        List<JobListing> resListings = (List<JobListing>) listings.getData();
        assertEquals(jobListing.getAuthorId(), resListings.get(0).getAuthorId());
    }
	
	@SuppressWarnings("unchecked")
	@Test
    void testListJobListingByAuthorIdisNotID() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        Long authorId = Long.valueOf(1);
        when(jobListingService.isId(String.valueOf(authorId))).thenReturn(false);
        //when(jobListingService.getJobListingById(id)).thenReturn(null);

        APIResponse listings = jobListingController.listJobListingByAuthorId(response, jobListing.getAuthorId());
        List<JobListing> resListings = (List<JobListing>) listings.getData();
        assertEquals(resListings, null);
        //assertEquals(jobListing.getId(), ((JobListing) listings.getData()).getId());
    }
	
	@SuppressWarnings("unchecked")
	@Test
    void testListJobListingByAuthorIdNull() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        Long authorId = Long.valueOf(1);
        when(jobListingService.isId(String.valueOf(authorId))).thenReturn(true);
        when(jobListingService.listJobListingByAuthorId(authorId)).thenReturn(null);

        APIResponse listings = jobListingController.listJobListingByAuthorId(response, jobListing.getAuthorId());
        List<JobListing> resListings = (List<JobListing>) listings.getData();
        assertEquals(resListings, null);
        //assertNull((JobListing) listings.getData());
        //assertEquals(jobListing.getId(), ((JobListing) listings.getData()).getId());
    }
	
	@SuppressWarnings("unchecked")
	@Test
    void testListJobListingByAuthorIdThrowException() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        Long authorId = Long.valueOf(1);
        when(jobListingService.isId(String.valueOf(authorId))).thenReturn(true);
        when(jobListingService.listJobListingByAuthorId(authorId)).thenThrow(UnexpectedRollbackException.class);

        APIResponse listings = jobListingController.listJobListingByAuthorId(response, jobListing.getAuthorId());
        List<JobListing> resListings = (List<JobListing>) listings.getData();
        assertNull(resListings);
    }
	
	//listJobListingByAuthorIdAndStatus
	
	@SuppressWarnings("unchecked")
	@Test
    void testListJobListingByAuthorIdAndStatusisNotStatus() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        Long authorId = Long.valueOf(1);
        String listingStatus = "";
        when(jobListingService.isStatusInvalid(listingStatus)).thenReturn(true);
        //when(JobListingStatusEnum.Constants.JOB_LISTING_STATUS_LIST.contains(listingStatus)).thenReturn(false);

        APIResponse res = jobListingController.listJobListingByAuthorIdAndStatus(response, authorId ,listingStatus);
        assertNull((List<JobListing>) res.getData());
    }
	
	
	
	//listJobListingByAuthorIdAndStatus
	
	@SuppressWarnings("unchecked")
	@Test
    void testListJobListingByAuthorIdAndStatusisNotId() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        Long authorId = Long.valueOf(1);
        String listingStatus = jobListings.get(0).getStatus();
        List<String> errors = new ArrayList<String>();
        errors.add("Invalid author id value");
//        when(StringUtils.isEmpty(listingStatus)).thenReturn(false);
//        when(JobListingStatusEnum.Constants.JOB_LISTING_STATUS_LIST.contains(listingStatus)).thenReturn(true);
        when(jobListingService.isId(String.valueOf("abc"))).thenReturn(false);
        //when(JobListingStatusEnum.Constants.JOB_LISTING_STATUS_LIST.contains(listingStatus)).thenReturn(false);
        //when(jobListingService.getJobListingById(id)).thenReturn(null);

        APIResponse res = jobListingController.listJobListingByAuthorIdAndStatus(response, authorId ,listingStatus);
//        List<JobListing> resListings = (List<JobListing>) res.getData();
        assertNull((List<JobListing>) res.getData());
    }
	
	@SuppressWarnings("unchecked")
	@Test
    void testListJobListingByAuthorIdAndStatusNull() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        Long authorId = Long.valueOf(1);
        String listingStatus = jobListings.get(0).getStatus();
        when(jobListingService.isStatusInvalid(listingStatus)).thenReturn(false);
        when(jobListingService.isId(String.valueOf(authorId))).thenReturn(true);
        when(jobListingService.listJobListingByAuthorIdAndStatus(authorId, listingStatus)).thenReturn(null);

        APIResponse res = jobListingController.listJobListingByAuthorIdAndStatus(response, authorId ,listingStatus);
//      List<JobListing> resListings = (List<JobListing>) res.getData();
        assertNull((List<JobListing>) res.getData());
    }
	
	@SuppressWarnings("unchecked")
	@Test
    void testListJobListingByAuthorIdAndStatusNoError() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        Long authorId = Long.valueOf(1);
        String listingStatus = jobListings.get(0).getStatus();
        when(jobListingService.isStatusInvalid(listingStatus)).thenReturn(false);
        when(jobListingService.isId(String.valueOf(authorId))).thenReturn(true);
        when(jobListingService.listJobListingByAuthorIdAndStatus(authorId, listingStatus)).thenReturn(jobListings);

        APIResponse res = jobListingController.listJobListingByAuthorIdAndStatus(response, authorId ,listingStatus);
        List<JobListing> resListings = (List<JobListing>) res.getData();
        assertEquals(resListings.get(0).getId(), jobListings.get(0).getId());
    }
	
	//listAllOpenActiveJobListing
	
	@SuppressWarnings("unchecked")
	@Test
    void testListAllOpenActiveJobListing() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        long pageNumber = Long.valueOf(1);
        String searchValue = "";
        String listingStatus = JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode();
        when(jobListingService.listAllOpenActiveJobListing(listingStatus, searchValue, ((int)pageNumber-1), numberOfListingPerPage)).thenReturn(jobListingsOpenStatus);

        APIResponse res = jobListingController.listAllOpenActiveJobListing(response, pageNumber, searchValue);
        List<JobListing> resListings = (List<JobListing>) res.getData();
        assertEquals(resListings.get(0).getId(), jobListingsOpenStatus.get(0).getId());
    }
	
	@SuppressWarnings("unchecked")
	@Test
    void testListAllOpenActiveJobListingNull() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        long pageNumber = Long.valueOf(1);
        String searchValue = "";
        String listingStatus = JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode();
        when(jobListingService.listAllOpenActiveJobListing(listingStatus, searchValue, ((int)pageNumber-1), numberOfListingPerPage)).thenReturn(null);

        APIResponse res = jobListingController.listAllOpenActiveJobListing(response, pageNumber, searchValue);
        List<JobListing> resListings = (List<JobListing>) res.getData();
        assertEquals(resListings, null);
    }
	
	@SuppressWarnings("unchecked")
	@Test
    void testListAllOpenActiveJobListingThrowException() throws URISyntaxException {    
        HttpServletResponse response = mock(HttpServletResponse.class); 
        long pageNumber = Long.valueOf(1);
        String searchValue = "";
        String listingStatus = JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode();
        when(jobListingService.listAllOpenActiveJobListing(listingStatus, searchValue, ((int)pageNumber-1), numberOfListingPerPage)).thenThrow(UnexpectedRollbackException.class);

        APIResponse res = jobListingController.listAllOpenActiveJobListing(response, pageNumber, searchValue);
        List<JobListing> resListings = (List<JobListing>) res.getData();
        assertNull(resListings);
    }
	
	//getAllOpenActiveJobListingTotal
	
		@SuppressWarnings("unchecked")
		@Test
	    void testGetAllOpenActiveJobListingTotal() throws URISyntaxException {    
	        HttpServletResponse response = mock(HttpServletResponse.class); 
	        String searchValue = "";
	        String listingStatus = JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode();
	        when(jobListingService.getAllOpenActiveJobListingTotal(listingStatus, searchValue)).thenReturn(jobListingsOpenStatus.size());

	        APIResponse res = jobListingController.getAllOpenActiveJobListingTotal(response, searchValue);
	        Integer resListingsTotal = (Integer) res.getData();
	        assertEquals(resListingsTotal, jobListingsOpenStatus.size());
	    }
		
		@SuppressWarnings("unchecked")
		@Test
	    void testGetAllOpenActiveJobListingTotalNull() throws URISyntaxException {    
	        HttpServletResponse response = mock(HttpServletResponse.class); 
	        String searchValue = "";
	        String listingStatus = JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode();
	        when(jobListingService.getAllOpenActiveJobListingTotal(listingStatus, searchValue)).thenReturn(null);

	        APIResponse res = jobListingController.getAllOpenActiveJobListingTotal(response, searchValue);
	        List<JobListing> resListings = (List<JobListing>) res.getData();
	        assertEquals(resListings, null);
	    }
		
		@SuppressWarnings("unchecked")
		@Test
	    void testGetAllOpenActiveJobListingTotalThrowException() throws URISyntaxException {    
	        HttpServletResponse response = mock(HttpServletResponse.class); 
	        long pageNumber = Long.valueOf(1);
	        String searchValue = "";
	        String listingStatus = JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode();
	        when(jobListingService.getAllOpenActiveJobListingTotal(listingStatus, searchValue)).thenThrow(UnexpectedRollbackException.class);

	        APIResponse res = jobListingController.getAllOpenActiveJobListingTotal(response, searchValue);
	        List<JobListing> resListings = (List<JobListing>) res.getData();
	        assertNull(resListings);
	    }
		
		@Test
	    void testCreateJobListing() {
			Status stat = new Status();
			stat.setStatusCode(Status.Type.OK.getCode());
			stat.setMessage("Successfully created job listing.");
			stat.setStatusText(Status.Type.OK.getText());
			
			HttpServletResponse response = mock(HttpServletResponse.class);
			when(jobListingService.isBlank(jobListingDTO.getTitle())).thenReturn(false);
			when(jobListingService.isBlank(jobListingDTO.getDetails())).thenReturn(false);
			when(jobListingService.isBlank(jobListingDTO.getRate())).thenReturn(false);
			when(jobListingService.isBlank(jobListingDTO.getRateType())).thenReturn(false);
	        when(jobListingService.isId(String.valueOf(jobListingDTO.getAuthorId()))).thenReturn(true);
	        when(jobListingService.addJobListing(jobListingDTO)).thenReturn(jobListing);
	        APIResponse res = jobListingController.createJobListing(response, jobListingDTO);
	        JobListing resListings = (JobListing) res.getData();
	        Status resStatus = res.getStatus();
	        verify(jobListingService, Mockito.times(1)).addJobListing(jobListingDTO);

	        assertEquals(resStatus.getStatusCode(), resStatus.getStatusCode());
	        assertEquals(resStatus.getStatusText(), resStatus.getStatusText());
	        assertEquals(resStatus.getMessage(), resStatus.getMessage());
	        assertEquals(jobListing.getId(), resListings.getId());
	    }
		@Test
	    void testCreateJobListingError() {
			HttpServletResponse response = mock(HttpServletResponse.class);
			when(jobListingService.isBlank(jobListingDTO.getTitle())).thenReturn(true);
			when(jobListingService.isBlank(jobListingDTO.getDetails())).thenReturn(true);
			when(jobListingService.isBlank(jobListingDTO.getRate())).thenReturn(true);
			when(jobListingService.isBlank(jobListingDTO.getRateType())).thenReturn(true);
	        when(jobListingService.isId(String.valueOf(jobListingDTO.getAuthorId()))).thenReturn(false);
	        //when(jobListingService.addJobListing(jobListing)).thenReturn(jobListing);
	        APIResponse res = jobListingController.createJobListing(response, jobListingDTO);
	        JobListing resListings = (JobListing) res.getData();
	        verify(jobListingService, Mockito.times(0)).addJobListing(jobListingDTO);

	        assertEquals(resListings, null);
	    }
		
		@Test
	    void testCreateJobListingNull() throws URISyntaxException {    
			HttpServletResponse response = mock(HttpServletResponse.class);
			when(jobListingService.isBlank(jobListingDTO.getTitle())).thenReturn(false);
			when(jobListingService.isBlank(jobListingDTO.getDetails())).thenReturn(false);
			when(jobListingService.isBlank(jobListingDTO.getRate())).thenReturn(false);
			when(jobListingService.isBlank(jobListingDTO.getRateType())).thenReturn(false);
	        when(jobListingService.isId(String.valueOf(jobListing.getAuthorId()))).thenReturn(true);
	        when(jobListingService.addJobListing(jobListingDTO)).thenReturn(null);
	        APIResponse res = jobListingController.createJobListing(response, jobListingDTO);
	        JobListing resListings = (JobListing) res.getData();
	        verify(jobListingService, Mockito.times(1)).addJobListing(jobListingDTO);

	        //assertEquals(jobListing.getId(), resListings.getId());
	        assertEquals(resListings, null);
	    }
		
		@Test
	    void testCreateJobListingThrowException() throws URISyntaxException {    
			HttpServletResponse response = mock(HttpServletResponse.class);
			when(jobListingService.isBlank(jobListingDTO.getTitle())).thenReturn(false);
			when(jobListingService.isBlank(jobListingDTO.getDetails())).thenReturn(false);
			when(jobListingService.isBlank(jobListingDTO.getRate())).thenReturn(false);
			when(jobListingService.isBlank(jobListingDTO.getRateType())).thenReturn(false);
	        when(jobListingService.isId(String.valueOf(jobListing.getAuthorId()))).thenReturn(true);
	        when(jobListingService.addJobListing(jobListingDTO)).thenThrow(UnexpectedRollbackException.class);
	        APIResponse res = jobListingController.createJobListing(response, jobListingDTO);
	        JobListing resListings = (JobListing) res.getData();
	        verify(jobListingService, Mockito.times(1)).addJobListing(jobListingDTO);

	        assertNull(resListings);
	    }
		
		@Test
	    void testUpdateJobListing() {
			when(jobListingService.isBlank(jobListing.getTitle())).thenReturn(false);
			when(jobListingService.isBlank(jobListing.getDetails())).thenReturn(false);
			when(jobListingService.isBlank(jobListing.getRate())).thenReturn(false);
			when(jobListingService.isBlank(jobListing.getRateType())).thenReturn(false);
	        when(jobListingService.isId(String.valueOf(jobListing.getAuthorId()))).thenReturn(true);
	        when(jobListingService.updateJobListing(jobListingDTO)).thenReturn(jobListing);
	        APIResponse res = jobListingController.updateJobListing(jobListingDTO.getId(), jobListingDTO);
	        JobListing resListings = (JobListing) res.getData();
	        verify(jobListingService, Mockito.times(1)).updateJobListing(jobListingDTO);

	        assertEquals(jobListing.getId(), resListings.getId());
	    }
		@Test
	    void testUpdateJobListingError() {
			when(jobListingService.isBlank(jobListing.getTitle())).thenReturn(true);
			when(jobListingService.isBlank(jobListing.getDetails())).thenReturn(true);
			when(jobListingService.isBlank(jobListing.getRate())).thenReturn(true);
			when(jobListingService.isBlank(jobListing.getRateType())).thenReturn(true);
	        when(jobListingService.isId(String.valueOf(jobListing.getAuthorId()))).thenReturn(false);
	        APIResponse res = jobListingController.updateJobListing(jobListingDTO.getId(), jobListingDTO);
	        JobListing resListings = (JobListing) res.getData();
	        verify(jobListingService, Mockito.times(0)).updateJobListing(jobListingDTO);

	        assertEquals(resListings, null);
	    }
		
		@Test
	    void testUpdateJobListingNull() throws URISyntaxException {    
			HttpServletResponse response = mock(HttpServletResponse.class);
			when(jobListingService.isBlank(jobListing.getTitle())).thenReturn(false);
			when(jobListingService.isBlank(jobListing.getDetails())).thenReturn(false);
			when(jobListingService.isBlank(jobListing.getRate())).thenReturn(false);
			when(jobListingService.isBlank(jobListing.getRateType())).thenReturn(false);
	        when(jobListingService.isId(String.valueOf(jobListing.getAuthorId()))).thenReturn(true);
	        when(jobListingService.updateJobListing(jobListingDTO)).thenReturn(null);
	        APIResponse res = jobListingController.updateJobListing(jobListingDTO.getId(), jobListingDTO);
	        JobListing resListings = (JobListing) res.getData();
	        verify(jobListingService, Mockito.times(1)).updateJobListing(jobListingDTO);

	        //assertEquals(jobListing.getId(), resListings.getId());
	        assertEquals(resListings, null);
	    }
		
		@Test
	    void testUpdateJobListingThrowException() throws URISyntaxException {
			when(jobListingService.isBlank(jobListing.getTitle())).thenReturn(false);
			when(jobListingService.isBlank(jobListing.getDetails())).thenReturn(false);
			when(jobListingService.isBlank(jobListing.getRate())).thenReturn(false);
			when(jobListingService.isBlank(jobListing.getRateType())).thenReturn(false);
	        when(jobListingService.isId(String.valueOf(jobListing.getAuthorId()))).thenReturn(true);
	        when(jobListingService.updateJobListing(jobListingDTO)).thenThrow(UnexpectedRollbackException.class);
	        APIResponse res = jobListingController.updateJobListing(jobListingDTO.getId(), jobListingDTO);
	        JobListing resListings = (JobListing) res.getData();
	        verify(jobListingService, Mockito.times(1)).updateJobListing(jobListingDTO);

	        assertNull(resListings);
	    }
		
		@Test
	    void testUpdateJobListingStatus() {
			when(jobListingService.isStatusInvalid(jobListing.getStatus())).thenReturn(false);
	        when(jobListingService.isId(String.valueOf(jobListing.getAuthorId()))).thenReturn(true);
	        when(jobListingService.updateJobListingStatus(jobListing.getId(), jobListing.getStatus())).thenReturn(jobListing);
	        APIResponse res = jobListingController.updateJobListingStatus(jobListing.getId(), jobListing.getStatus());
	        JobListing resListings = (JobListing) res.getData();
	        verify(jobListingService, Mockito.times(1)).updateJobListingStatus(jobListing.getId(), jobListing.getStatus());

	        assertEquals(jobListing.getId(), resListings.getId());
	    }
		@Test
	    void testUpdateJobListingStatusError() {
			when(jobListingService.isStatusInvalid(jobListing.getStatus())).thenReturn(true);
	        when(jobListingService.isId(String.valueOf(jobListing.getAuthorId()))).thenReturn(false);
	        APIResponse res = jobListingController.updateJobListingStatus(jobListing.getId(), jobListing.getStatus());
	        JobListing resListings = (JobListing) res.getData();
	        verify(jobListingService, Mockito.times(0)).updateJobListingStatus(jobListing.getId(), jobListing.getStatus());

	        assertEquals(resListings, null);
	    }
		
		@Test
	    void testUpdateJobListingStatusNull() throws URISyntaxException {    
			when(jobListingService.isStatusInvalid(jobListing.getStatus())).thenReturn(false);
	        when(jobListingService.isId(String.valueOf(jobListing.getAuthorId()))).thenReturn(true);
	        when(jobListingService.updateJobListingStatus(jobListing.getId(), jobListing.getStatus())).thenReturn(null);
	        APIResponse res = jobListingController.updateJobListingStatus(jobListing.getId(), jobListing.getStatus());
	        JobListing resListings = (JobListing) res.getData();
	        verify(jobListingService, Mockito.times(1)).updateJobListingStatus(jobListing.getId(), jobListing.getStatus());

	        assertEquals(resListings, null);
	    }
		
		@Test
	    void testUpdateJobListingStatusThrowException() throws URISyntaxException {
			when(jobListingService.isStatusInvalid(jobListing.getStatus())).thenReturn(false);
	        when(jobListingService.isId(String.valueOf(jobListing.getAuthorId()))).thenReturn(true);
	        when(jobListingService.updateJobListingStatus(jobListing.getId(), jobListing.getStatus())).thenThrow(UnexpectedRollbackException.class);
	        APIResponse res = jobListingController.updateJobListingStatus(jobListing.getId(), jobListing.getStatus());
	        JobListing resListings = (JobListing) res.getData();
	        verify(jobListingService, Mockito.times(1)).updateJobListingStatus(jobListing.getId(), jobListing.getStatus());

	        assertNull(resListings);
	    }
		
		@Test
	    void testGetCompletedJobListingById() throws URISyntaxException {    
	        HttpServletResponse response = mock(HttpServletResponse.class); 
	        Long id = Long.valueOf(1);
	        when(jobListingService.isId(String.valueOf(id))).thenReturn(true);
	        when(jobListingService.getJobListingById(id)).thenReturn(jobListing);

	        APIResponse listings = jobListingController.getCompletedJobListingById(response, jobListing.getId());
	        assertEquals(jobListing.getId(), ((JobListing) listings.getData()).getId());
	    }
		
		@Test
	    void testGetCompletedJobListingByIdisNotID() throws URISyntaxException {    
	        HttpServletResponse response = mock(HttpServletResponse.class); 
	        Long id = Long.valueOf(1);
	        when(jobListingService.isId(String.valueOf(id))).thenReturn(false);
	        APIResponse listings = jobListingController.getCompletedJobListingById(response, jobListing.getId());
	        assertEquals(((JobListing) listings.getData()), null);
		}
		
		@Test
	    void testGetCompletedJobListingByIdNull() throws URISyntaxException {    
	        HttpServletResponse response = mock(HttpServletResponse.class); 
	        Long id = Long.valueOf(1);
	        when(jobListingService.isId(String.valueOf(id))).thenReturn(true);
	        when(jobListingService.getJobListingById(id)).thenReturn(null);

	        APIResponse listings = jobListingController.getCompletedJobListingById(response, jobListing.getId());
	        assertEquals(((JobListing) listings.getData()), null);
	    }
		
		@Test
	    void testGetCompletedJobListingByIdThrowException() throws URISyntaxException {    
	        HttpServletResponse response = mock(HttpServletResponse.class); 
	        Long id = Long.valueOf(1);
	        when(jobListingService.isId(String.valueOf(id))).thenReturn(true);
	        when(jobListingService.getJobListingById(id)).thenThrow(UnexpectedRollbackException.class);
	        
	        APIResponse listings = jobListingController.getCompletedJobListingById(response, jobListing.getId());
	        assertNull((JobListing) listings.getData());
	    }
}
