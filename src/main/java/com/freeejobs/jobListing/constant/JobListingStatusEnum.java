package com.freeejobs.jobListing.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum JobListingStatusEnum {
	OPEN_FOR_APPLICATION("OFA","Open For Application"), PENDING_COMPLETION("PC", "Pending Completion"), COMPLETED("C", "Completed"), REMOVED("R","Removed");
	
	public static class Constants{
		private Constants() {
		}
		
		public static final List<String> JOB_LISTING_STATUS_LIST =Collections.unmodifiableList(Arrays.asList(
				OPEN_FOR_APPLICATION.getCode(), PENDING_COMPLETION.getCode(), COMPLETED.getCode(), REMOVED.getCode()));
	}
	
	private String code;
	private String description;
	
	JobListingStatusEnum(final String code, String description){
		this.code=code;
		this.description=description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
