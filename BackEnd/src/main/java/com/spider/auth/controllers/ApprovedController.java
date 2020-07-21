package com.spider.auth.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spider.auth.models.Approved;
import com.spider.auth.models.Planning;
import com.spider.auth.repository.ApprovedRepository;
import com.spider.auth.repository.PlanningRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class ApprovedController {
	@Autowired
	ApprovedRepository approvedRepository;
	
	@Autowired
	PlanningRepository planningRepository;
	
	@GetMapping("/approved")
	public ResponseEntity<List<Approved>> listAllApproveds(){
		try {
			List<Approved> approved = new ArrayList<Approved>();
			approvedRepository.findAll().forEach(approved::add);
			
			if (approved.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(approved, HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/approved/{approved_id}")
	public ResponseEntity<Approved> getApprovedById(@PathVariable("approved_id") long id) {
		Optional<Approved> approvedData = approvedRepository.findById(id);

		if (approvedData.isPresent()) {
			return new ResponseEntity<>(approvedData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/approved/{planning_id}")
	//@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Approved> createApproved(@PathVariable("planning_id") Long id, @RequestBody Approved approved){
		Optional<Planning> planningData = planningRepository.findById(id);
		
		if(planningData.isPresent()) {
			Planning planning = planningData.get();
			Approved _approved = approvedRepository
					.save(new Approved(approved.getNewRevisionDate(), approved.getNewSubmissionDate(), approved.getNewStatus(), planning, approved.getCorrections(), approved.getIsRegistered()));
		
			return new ResponseEntity<>(approvedRepository.save(_approved), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/approved/{approved_id}")
	//@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Approved> updateApproved(@PathVariable("approved_id") long id, @RequestBody Approved approved) {
		Optional<Approved> approvedData = approvedRepository.findById(id);

		if (approvedData.isPresent()) {
			Approved _approved = approvedData.get();
			_approved.setNewRevisionDate(approved.getNewRevisionDate());
			_approved.setNewSubmissionDate(approved.getNewSubmissionDate());
			_approved.setNewStatus(approved.getNewStatus());
                        _approved.setCorrections(approved.getCorrections()); 
			
			return new ResponseEntity<>(approvedRepository.save(_approved), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/approved/{approved_id}/{new_isRegistered}")
	//@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Approved> updateApproved(@PathVariable("approved_id") long id, @PathVariable("new_isRegistered") int new_isRegistered) {
		Optional<Approved> approvedData = approvedRepository.findById(id);

		if (approvedData.isPresent()) {
			Approved _approved = approvedData.get();
			_approved.setIsRegistered(new_isRegistered);
			
			return new ResponseEntity<>(approvedRepository.save(_approved), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
        
//	@PreAuthorize("hasRole('ADMIN')")
//	public ResponseEntity<HttpStatus> deleteApproved(@PathVariable("vehicle_id") long index, @PathVariable("calendar_id") long id) {
//		try {
//			Optional<Planning> planningData = planningRepository.findById(index);
//			Planning planning= planningData.get();
//			Optional<Approved> approvedData = approvedRepository.findById(id);
//			Approved _approved = approvedData.get();
//
//			planning.getApproved().remove;
//
//			approvedRepository.deleteById(id);
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
//		}
//	}
        
        @DeleteMapping("/approved/{approved_id}")
	  //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	  public ResponseEntity<HttpStatus> deleteApproved(@PathVariable("approved_id") long id) {
	    try {
	    	approvedRepository.deleteById(id);
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	    }
	  }

	@DeleteMapping("/approved")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deleteAllApproveds() {
		try {
			approvedRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}
        
        
}
