package com.spider.auth.models;

import javax.persistence.*;

@Entity
@Table(name = "planning_approved")
public class Approved {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne
        @JoinColumn(name = "planning_id")
	private Planning planning;
        
        private String newRevisionDate;
	private String newSubmissionDate;
	private String newStatus;
        private String corrections;
        private int isRegistered;
	
	public Approved() {
		super();
	}
	
	public Approved(String newRevisionDate, String newSubmissionDate, String newStatus, Planning planning, String corrections, int isRegistered) {
		super();
		this.newRevisionDate = newRevisionDate;
		this.newSubmissionDate = newSubmissionDate;
		this.newStatus = newStatus;
                this.planning = planning;
                this.corrections = corrections;
                this.isRegistered = isRegistered;
	}
	
	public Planning getPlanning() {
		return planning;
	}

	public void setPlanning(Planning planning) {
		this.planning = planning;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNewRevisionDate() {
		return newRevisionDate;
	}
	public void setNewRevisionDate(String newRevisionDate) {
		this.newRevisionDate = newRevisionDate;
	}
	public String getNewSubmissionDate() {
		return newSubmissionDate;
	}
	public void setNewSubmissionDate(String newSubmissionDate) {
		this.newSubmissionDate = newSubmissionDate;
	}
	public String getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}
        public void setCorrections(String corrections) {
                this.corrections = corrections;
        }
        public String getCorrections() {
                return corrections;
        }
        public void setIsRegistered(int isRegistered) {
                this.isRegistered = isRegistered;
        }
        
        public int getIsRegistered(){
            return isRegistered;
        }
}

