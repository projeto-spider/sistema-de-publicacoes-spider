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

import com.spider.auth.models.Planning;
import com.spider.auth.models.User;
import com.spider.auth.models.Vehicle;
import com.spider.auth.repository.UserRepository;
import com.spider.auth.repository.VehicleRepository;
import com.spider.auth.repository.PlanningRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class PlanningController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	VehicleRepository vehicleRepository;

	@Autowired
	PlanningRepository planningRepository;

	@GetMapping("/planning")
	public ResponseEntity<List<Planning>> getAllPlans(@RequestParam(required = false) String name) {
		try {
			List<Planning> plans = new ArrayList<Planning>();

			if (name == null) planningRepository.findAll().forEach(plans::add);

			if (plans.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(plans, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/planning/{planning_id}")
	public ResponseEntity<Planning> getUserById(@PathVariable("planning_id") long id) {
		Optional<Planning> planningData = planningRepository.findById(id);

		if (planningData.isPresent()) {
			return new ResponseEntity<>(planningData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/planning/{user_id}_{vehicle_id}")
	//@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Planning> createPlanning(@PathVariable("user_id") long Uid,@PathVariable("vehicle_id") long Vid, @RequestBody Planning planning) {
		Optional<User> userData = userRepository.findById(Uid);
		Optional<Vehicle> vehicleData = vehicleRepository.findById(Vid);
		if (userData.isPresent()) {
			User user = userData.get();
			Vehicle vehicle = vehicleData.get();
			Planning _planning = new Planning(user,vehicle, planning.getTitle(), planning.getStructure(), planning.getSubmissionDate(),planning.getRevisionDate(), planning.getStatus(), planning.getResubmitted(), planning.getVehicleResubmitted(), planning.getIsRegistered(), planning.getYear(), planning.getJustification());
			
			return new ResponseEntity<>(planningRepository.save(_planning), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}
        
        @PutMapping("/planning/{planning_id}_{new_resubmission}_{vehicle_resubmitted}")
        public ResponseEntity<Planning> updatePlanningResubStatus(@PathVariable("planning_id") long id, @PathVariable("new_resubmission") int new_resubmission, @PathVariable("vehicle_resubmitted") String vehicle_resubmitted) {
		Optional<Planning> planningData = planningRepository.findById(id);

                
		if (planningData.isPresent()) {
			Planning _planning = planningData.get();
                        _planning.setResubmitted(new_resubmission);
                        _planning.setVehicleResubmitted(vehicle_resubmitted);

			return new ResponseEntity<>(planningRepository.save(_planning), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
        
        @PutMapping("/planning/{planning_id}/{new_isRegistered}")
        public ResponseEntity<Planning> updateIsRegistered(@PathVariable("planning_id") long id, @PathVariable("new_isRegistered") int new_isRegistered) {
		Optional<Planning> planningData = planningRepository.findById(id);

                
		if (planningData.isPresent()) {
			Planning _planning = planningData.get();
                        _planning.setIsRegistered(new_isRegistered);

			return new ResponseEntity<>(planningRepository.save(_planning), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
        

	@PutMapping("/planning/{planning_id}/{user_id}/{vehicle_id}")
//	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Planning> updatePlanning(@PathVariable("planning_id") long id, @PathVariable("user_id") long Uid, @PathVariable("vehicle_id") long Vid, @RequestBody Planning planning) {
		Optional<Planning> planningData = planningRepository.findById(id);
                Optional<User> userData = userRepository.findById(Uid);
                Optional<Vehicle> vehicleData = vehicleRepository.findById(Vid);
                
                
		if (planningData.isPresent()) {
			Planning _planning = planningData.get();
                        User user = userData.get();
                        Vehicle vehicle = vehicleData.get();
			_planning.setTitle(planning.getTitle());
			_planning.setStructure(planning.getStructure());
			_planning.setSubmissionDate(planning.getSubmissionDate());
			_planning.setRevisionDate(planning.getRevisionDate());
			_planning.setStatus(planning.getStatus());
                        _planning.setUser(user);
                        _planning.setVehicle(vehicle);
                        _planning.setResubmitted(planning.getResubmitted());
                        _planning.setYear(planning.getYear());
                        _planning.setJustification(planning.getJustification());

			return new ResponseEntity<>(planningRepository.save(_planning), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/planning/{planning_id}")
	//@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deletePlanning(@PathVariable("planning_id") long id) {
		try {
			planningRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/planning")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deleteAllPlanning() {
		try {
			planningRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}
}
