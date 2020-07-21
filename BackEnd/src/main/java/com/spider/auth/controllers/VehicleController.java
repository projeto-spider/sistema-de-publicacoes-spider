package com.spider.auth.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.spider.auth.models.Vehicle;
import com.spider.auth.repository.VehicleRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class VehicleController {
	@Autowired
	VehicleRepository vehicleRepository;

	  @GetMapping("/vehicles")
	  public ResponseEntity<List<Vehicle>> getAllVehicles(@RequestParam(required = false) String name) {
	    try {
	      List<Vehicle> vehicles = new ArrayList<Vehicle>();

	      if (name == null)
	    	  vehicleRepository.findAll().forEach(vehicles::add);
	      else
	    	  vehicleRepository.findByNameContaining(name).forEach(vehicles::add);

	      if (vehicles.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }

	      return new ResponseEntity<>(vehicles, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	  @GetMapping("/vehicles/{vehicle_id}")
	  public ResponseEntity<Vehicle> getVehicleById(@PathVariable("vehicle_id") long id) {
	    Optional<Vehicle> vehicleData = vehicleRepository.findById(id);

	    if (vehicleData.isPresent()) {
	      return new ResponseEntity<>(vehicleData.get(), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  @PostMapping("/vehicles")
	  //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	  public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
	    try {
	    	Vehicle _vehicle = vehicleRepository
	          .save(new Vehicle(vehicle.getType(), vehicle.getName(), vehicle.getQualis(), vehicle.getLine(), vehicle.getCalendar(), vehicle.getUrl()));
	      return new ResponseEntity<>(_vehicle, HttpStatus.CREATED);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
	    }
	  }

	  @PutMapping("/vehicles/{vehicle_id}")
	  //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	  public ResponseEntity<Vehicle> updateVehicle(@PathVariable("vehicle_id") long id, @RequestBody Vehicle vehicle) {
	    Optional<Vehicle> vehicleData = vehicleRepository.findById(id);

	    if (vehicleData.isPresent()) {
	    	Vehicle _vehicle = vehicleData.get();
	    	_vehicle.setType(vehicle.getType());
	    	_vehicle.setName(vehicle.getName());
	    	_vehicle.setQualis(vehicle.getQualis());
	    	_vehicle.setLine(vehicle.getLine());
                _vehicle.setUrl(vehicle.getUrl());
	      
	      return new ResponseEntity<>(vehicleRepository.save(_vehicle), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  @DeleteMapping("/vehicles/{vehicle_id}")
	 // @PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<HttpStatus> deleteVehicle(@PathVariable("vehicle_id") long id) {
	    try {
	    	vehicleRepository.deleteById(id);
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	    }
	  }
	  
	  @DeleteMapping("/vehicles")
	  //@PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<HttpStatus> deleteAllVehicles() {
	    try {
	    	vehicleRepository.deleteAll();
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	    }

	  }

	  @GetMapping("/vehicles/type")
	  public ResponseEntity<List<Vehicle>> findByType(@RequestParam(required = false) String type) {
		    try {
		      List<Vehicle> vehicles = vehicleRepository.findByType("Conferência");

		      if (vehicles.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		      }
		      return new ResponseEntity<>(vehicles, HttpStatus.OK);
		    } catch (Exception e) {
		      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		    }
		  }
}
