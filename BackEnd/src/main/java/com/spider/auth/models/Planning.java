package com.spider.auth.models;

import javax.persistence.*;

@Entity
@Table(name = "users_planning_vehicles")
public class Planning{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;

	@ManyToOne
	@JoinColumn(name = "vehicle_id")
	Vehicle vehicle;
	
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "planning_approved_id")
//	private Approved approved;

	private String title;
	private String structure;
	private String submissionDate;
	private String revisionDate;
	private String status;
        private int resubmitted;
        private String vehicleResubmitted;
        private int isRegistered;
        private String year;
        private String justification;

	public Planning() {
		super();
	}

	public Planning(User user, Vehicle vehicle, String title, String structure, String submissionDate,
			String revisionDate, String status, int resubmitted, String vehicleResubmitted, int isRegistered, String year, String justification) {
		super();
		this.user = user;
		this.vehicle = vehicle;
		this.title = title;
		this.structure = structure;
		this.submissionDate = submissionDate;
		this.revisionDate = revisionDate;
		this.status = status;
                this.resubmitted = resubmitted;
                this.vehicleResubmitted = vehicleResubmitted;
                this.isRegistered = isRegistered;
                this.year = year;
                this.justification = justification;
	}
	
	
//	public Approved getApproved() {
//		return approved;
//	}
//
//	public void setApproved(Approved approved) {
//		this.approved = approved;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	public String getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(String submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getRevisionDate() {
		return revisionDate;
	}

	public void setRevisionDate(String revisionDate) {
		this.revisionDate = revisionDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
        
        public void setResubmitted(int resubmitted) {
		this.resubmitted = resubmitted;
	}
        
        public int getResubmitted() {
		return resubmitted;
	}
        
        public void setVehicleResubmitted(String vehicleResubmitted) {
            this.vehicleResubmitted = vehicleResubmitted;
        }
        
        public String getVehicleResubmitted() {
            return vehicleResubmitted;
        }
        
        public void setIsRegistered(int isRegistered) {
            this.isRegistered = isRegistered;
        }
        public int getIsRegistered() {
            return isRegistered;
        }
        public String getYear(){
            return year;
        }
        public void setYear(String year) {
            this.year = year;
        }
        public String getJustification() {
            return justification;
        }
        public void setJustification(String justification){
            this.justification = justification;
        }
}
