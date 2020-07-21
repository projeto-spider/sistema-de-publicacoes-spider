package com.spider.auth.models;

import javax.persistence.*;

@Entity
@Table(name = "calendar")
public class Calendar {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long calendar_id;

	@Column(name = "startDate")
	private String startDate;

	@Column(name = "endDate")
	private String endDate;

	@Column(name = "submissionDate")
	private String submissionDate;

	@Column(name = "callForPaper")
	private String callForPaper;
	
	@Column(name = "place")
	private String place;
        
        @Column(name = "year")
	private String year;

	@Column(name = "deadlineReminded", columnDefinition = "boolean default false")
	private boolean deadlineReminded = false;

	@Column(name = "startReminded", columnDefinition = "boolean default false")
	private boolean startReminded = false;

	//	@ManyToMany(mappedBy = "calendar")
	//	private Set<Vehicle> vehicles;

	//	@ManyToMany
	//	@JoinTable(
	//	  name = "calendar_event", 
	//	  joinColumns = @JoinColumn(name = "calendar_id"), 
	//	  inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
	//	Set<Vehicle> has_events;

	public Calendar(String startDate, String endDate, String submissionDate, String callForPaper, String place, String year) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.place = place;
		this.submissionDate=submissionDate;
		this.callForPaper = callForPaper;
                this.year = year;
	}

	public Calendar() {
		super();
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(String submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Long getId() {
		return calendar_id;
	}

	public boolean isDeadlineReminded() {
		return deadlineReminded;
	}

	public void setDeadlineReminded(boolean deadlineReminded) {
		this.deadlineReminded = deadlineReminded;
	}

	public boolean isStartReminded() {
		return startReminded;
	}

	public void setStartReminded(boolean startReminded) {
		this.startReminded = startReminded;
	}

	public String getCallForPaper() {
		return callForPaper;
	}

	public void setCallForPaper(String callForPaper) {
		this.callForPaper = callForPaper;
	}
        
        public String getYear() {
		return year;
	}
        
        public void setYear(String year){
            this.year = year;
        }
	
}
