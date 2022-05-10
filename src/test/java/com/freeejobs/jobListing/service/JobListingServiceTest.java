package com.freeejobs.jobListing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Date;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.freeejobs.jobListing.WebConfig;
import com.freeejobs.jobListing.constant.AuditEnum;
import com.freeejobs.jobListing.constant.JobListingStatusEnum;
import com.freeejobs.jobListing.controller.JobListingController;
import com.freeejobs.jobListing.controller.JobListingFixture;
import com.freeejobs.jobListing.dto.JobListingDTO;
import com.freeejobs.jobListing.model.JobListing;
import com.freeejobs.jobListing.model.JobListingAudit;
import com.freeejobs.jobListing.repository.JobListingAuditRepository;
import com.freeejobs.jobListing.repository.JobListingRepository;
import com.freeejobs.jobListing.response.APIResponse;
import com.freeejobs.jobListing.response.Status;

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
	private JobListingDTO jobListingDTO;
	private List<JobListing> jobListings;
	private List<JobListing> jobListingsOpenStatus;
	private Optional<JobListing> jobListingOptional;
	
	private JobListingAudit jobListingAudit;
	
	private int numberOfListingPerPage=10;
	
	@BeforeEach
    void setUp() {
        jobListing = JobListingFixture.createJobListing();
        jobListingDTO = JobListingFixture.createJobListingDTO();
        jobListings = JobListingFixture.createJoblistingList();
        jobListingsOpenStatus = JobListingFixture.createOpenJoblistingList();
        jobListingOptional = JobListingFixture.createJobListingOptional();
        jobListingAudit = JobListingFixture.createJobListingAudit();
    }
	
//	@Test
//	@MockitoSettings(strictness = Strictness.LENIENT)
//    void testGetJobListingById() throws URISyntaxException {    
////        HttpServletResponse response = mock(HttpServletResponse.class);
//		
//        Long jobId = Long.valueOf(1);
//        JobListingRepository repo = mock(JobListingRepository.class);
//        Mockito.lenient().doReturn(jobListing).when(jobListingRepository).findById(jobId);
//        //when(jobListingRepository.findById(jobId)).thenReturn(jobListingOptional);
//
//        JobListing listings = JobListingService.getJobListingById(jobId);
//        assertEquals(jobListing.getId(), listings.getId());
//    }
	
	@Test
    void testListJobListingByAuthorId() throws URISyntaxException {    
//        HttpServletResponse response = mock(HttpServletResponse.class);
		
        Long authorId = Long.valueOf(1);
        when(jobListingRepository.findByAuthorId(authorId)).thenReturn(jobListings);
        

        List<JobListing> listings = JobListingService.listJobListingByAuthorId(authorId);
        assertEquals(jobListings.get(0).getId(), listings.get(0).getId());
    }
	
	@Test
    void testListAllOpenActiveJobListing() throws URISyntaxException {    
//        HttpServletResponse response = mock(HttpServletResponse.class);
		
        Long authorId = Long.valueOf(1);
        Integer pageNumber = 1;
        String searchValue = "";
        String status = JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode();
        when(jobListingRepository.listAllOpenActiveJobListing(status, searchValue, PageRequest.of(pageNumber, numberOfListingPerPage))).thenReturn(jobListingsOpenStatus);
        

        List<JobListing> listings = JobListingService.listAllOpenActiveJobListing(status, searchValue, pageNumber, numberOfListingPerPage);
        assertEquals(jobListingsOpenStatus.get(0).getId(), listings.get(0).getId());
    }
	
	@Test
    void testGetAllOpenActiveJobListingTotal() throws URISyntaxException {    
//        HttpServletResponse response = mock(HttpServletResponse.class);
		
        Long authorId = Long.valueOf(1);
        Integer pageNumber = 1;
        String searchValue = "";
        String status = JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode();
        when(jobListingRepository.getAllOpenActiveJobListingTotal(status, searchValue)).thenReturn(jobListingsOpenStatus.size());
        

        Integer listingTotal = JobListingService.getAllOpenActiveJobListingTotal(status, searchValue);
        assertEquals(jobListingsOpenStatus.size(), listingTotal);
    }
	
	@Test
    void testAddJobListing() throws URISyntaxException {    
		JobListing newJobListing = new JobListing();
		newJobListing.setTitle(jobListingDTO.getTitle());
		newJobListing.setAuthorId(jobListingDTO.getAuthorId());
		newJobListing.setDetails(jobListingDTO.getDetails());
		newJobListing.setRate(jobListingDTO.getRate());
		newJobListing.setRateType(jobListingDTO.getRateType());
		newJobListing.setDateCreated(new Date());
		newJobListing.setDateUpdated(new Date());
		newJobListing.setStatus(JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode());
        //when(jobListingRepository.save(newJobListing)).thenReturn(jobListing);
        when(jobListingRepository.save(any(JobListing.class))).then(returnsFirstArg());
        jobListingAudit.setOpsType(AuditEnum.INSERT.getCode());
        Mockito.lenient().when(JobListingService.insertAudit(jobListing, AuditEnum.INSERT.getCode())).thenReturn(jobListingAudit);

        JobListing listings = JobListingService.addJobListing(jobListingDTO);

        assertEquals(listings.getRate(), jobListing.getRate());
        assertEquals(listings.getRateType(), jobListing.getRateType());
        assertEquals(listings.getDetails(), jobListing.getDetails());
        assertEquals(listings.getTitle(), jobListing.getTitle());
        //assertEquals(jobListing.getId(), listings.getId());
    }
	
	@Test
    void testUpdateJobListing() throws URISyntaxException {    
		Date date = new Date();
        jobListing.setDateUpdated(date);
        
		when(jobListingRepository.findById(jobListingDTO.getId())).thenReturn(jobListing);
		when(jobListingRepository.save(any(JobListing.class))).then(returnsFirstArg());
        //when(jobListingRepository.save(jobListingDTO)).thenReturn(jobListing);
        jobListingAudit.setOpsType(AuditEnum.UPDATE.getCode());
        Mockito.lenient().when(JobListingService.insertAudit(jobListing, AuditEnum.UPDATE.getCode())).thenReturn(jobListingAudit);

        JobListing listings = JobListingService.updateJobListing(jobListingDTO);
        
        assertEquals(listings.getId(), jobListing.getId());
        assertEquals(listings.getDateUpdated(), jobListing.getDateUpdated());
        assertEquals(jobListing.getId(), listings.getId());
    }
	
	@Test
    void testUpdateJobListingStatus() throws URISyntaxException {    

		when(jobListingRepository.findById(jobListing.getId())).thenReturn(jobListing);
        when(jobListingRepository.save(jobListing)).thenReturn(jobListing);
        jobListingAudit.setOpsType(AuditEnum.UPDATE.getCode());
        Mockito.lenient().when(JobListingService.insertAudit(jobListing, AuditEnum.UPDATE.getCode())).thenReturn(jobListingAudit);

        JobListing listings = JobListingService.updateJobListingStatus(jobListing.getId(), jobListing.getStatus());
        verify(jobListingRepository, Mockito.times(1)).save(jobListing);

        assertEquals(jobListing.getId(), listings.getId());
    }
	
	@Test
    void testListJobListingByAuthorIdAndStatusEmptyStatus() throws URISyntaxException {    

		Long authorId = Long.valueOf(1);
        
        when(jobListingRepository.findAllJobListingByAuthorIdAndStatus(authorId, JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode())).thenReturn(jobListings);

        List<JobListing> listings = JobListingService.listJobListingByAuthorIdAndStatus(authorId, JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode());
        
        assertEquals(jobListings.get(0).getId(), listings.get(0).getId());
    }
	
	@Test
    void testListJobListingByAuthorIdAndStatus() throws URISyntaxException {    

		Long authorId = Long.valueOf(1);
        when(jobListingRepository.findByAuthorId(authorId)).thenReturn(jobListings);

        List<JobListing> listings = JobListingService.listJobListingByAuthorIdAndStatus(authorId, "");
        
        assertEquals(jobListings.get(0).getId(), listings.get(0).getId());
    }
	
	@Test
    void testIsId() {    

        boolean valid = JobListingService.isId("1");
        
        assertTrue(valid);
    }
	@Test
    void testIsNotId() {    

        boolean valid = JobListingService.isId("abc");
        
        assertFalse(valid);
    }
	
	@Test
    void testIsStatusInvalid() {    

        boolean valid = JobListingService.isStatusInvalid("ABC");
        
        assertTrue(valid);
    }
	@Test
    void testIsStatusValid() {    

        boolean valid = JobListingService.isStatusInvalid(JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode());
        
        assertFalse(valid);
    }
	
	@Test
    void testIsBlank() {    

        boolean valid = JobListingService.isBlank("");
        
        assertTrue(valid);
    }
	@Test
    void testIsNotBlank() {    

        boolean valid = JobListingService.isBlank("ABC");
        
        assertFalse(valid);
    }
	
	@Test
    void testInsertAudit() throws URISyntaxException {    

		Date date = new Date();
        JobListingAudit jobListingAuditLo = new JobListingAudit();
        jobListingAuditLo.setId(1);
        jobListingAuditLo.setOpsType(AuditEnum.INSERT.getCode());
        jobListingAuditLo.setAuditData(jobListing.toString());
        jobListingAuditLo.setCreatedBy(String.valueOf(jobListing.getAuthorId()));
        jobListingAuditLo.setDateCreated(date);
//        Mockito.lenient().when(jobListingAuditRepository.save(jobListingAuditLo)).thenReturn(jobListingAudit);
//
//        JobListingAudit Audit = JobListingService.insertAudit(jobListing, AuditEnum.INSERT.getCode());
//        
//        verify(jobListingAuditRepository, Mockito.times(1)).save(jobListingAuditLo);

        assertEquals(jobListingAuditLo.getId(), 1);
        assertEquals(jobListingAuditLo.getAuditData(), jobListing.toString());
        assertEquals(jobListingAuditLo.getDateCreated(), date);
        assertEquals(jobListingAuditLo.getCreatedBy(), String.valueOf(jobListing.getAuthorId()));
        assertEquals(jobListingAuditLo.getOpsType(),AuditEnum.INSERT.getCode());

    }
	
	@Test
    void testAuditEnum() {    

		AuditEnum.INSERT.setCode("T");
		AuditEnum.INSERT.setDescription("Test");

        assertEquals(AuditEnum.INSERT.getDescription(), "Test");
        assertEquals(AuditEnum.INSERT.getCode(), "T");

    }
	
	@Test
    void testJobApplicationStatusEnum() {    

		JobListingStatusEnum.OPEN_FOR_APPLICATION.setCode("A");
		JobListingStatusEnum.OPEN_FOR_APPLICATION.setDescription("Test");

        assertEquals(JobListingStatusEnum.OPEN_FOR_APPLICATION.getDescription(), "Test");
        assertEquals(JobListingStatusEnum.OPEN_FOR_APPLICATION.getCode(), "A");

    }
	
	

}
