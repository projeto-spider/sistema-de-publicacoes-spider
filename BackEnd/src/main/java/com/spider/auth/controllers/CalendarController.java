package com.spider.auth.controllers;

import java.net.*; 
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

import com.spider.auth.repository.CalendarRepository;
import com.spider.auth.repository.VehicleRepository;
import com.spider.auth.models.Calendar;
import com.spider.auth.models.Vehicle;

@CrossOrigin(origins = "*", maxAge = 3600)
//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class CalendarController {
	@Autowired
	CalendarRepository calendarRepository;

	@Autowired
	VehicleRepository vehicleRepository;

	@GetMapping("/calendar")
	public ResponseEntity<List<Calendar>> getAllCalendars(@RequestParam(required = false) String name) {
		try {
			List<Calendar> calendar = new ArrayList<Calendar>();

			if (name == null)
				calendarRepository.findAll().forEach(calendar::add);
			else
				calendarRepository.findByPlace(name).forEach(calendar::add);

			if (calendar.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(calendar, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/calendar/{event_id}")
	public ResponseEntity<Calendar> getCalendarById(@PathVariable("event_id") long id) {
		Optional<Calendar> calendarData = calendarRepository.findById(id);

		if (calendarData.isPresent()) {
			return new ResponseEntity<>(calendarData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/calendar/{vehicle_id}")
	//@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Calendar> createCalendar(@PathVariable("vehicle_id") long id, @RequestBody Calendar calendar) {
		Optional<Vehicle> vehicleData = vehicleRepository.findById(id);

		if (vehicleData.isPresent()) {
			Vehicle vehicle = vehicleData.get();
			Calendar _calendar = calendarRepository
					.save(new Calendar(calendar.getStartDate(), calendar.getEndDate(), calendar.getSubmissionDate(), calendar.getCallForPaper(), calendar.getPlace(), calendar.getYear()));
			vehicle.getCalendar().add(_calendar);
			return new ResponseEntity<>(calendarRepository.save(_calendar), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/calendar/{event_id}")
	//@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Calendar> updateCalendar(@PathVariable("event_id") long id, @RequestBody Calendar event) {
		Optional<Calendar> eventData = calendarRepository.findById(id);

		if (eventData.isPresent()) {
			Calendar _event = eventData.get();
			_event.setStartDate(event.getStartDate());
			_event.setEndDate(event.getEndDate());
			_event.setSubmissionDate(event.getSubmissionDate());
			_event.setCallForPaper(event.getCallForPaper());
			_event.setPlace(event.getPlace());
			_event.setDeadlineReminded(false);
			_event.setStartReminded(false);
                        _event.setYear(event.getYear());
                        
			return new ResponseEntity<>(calendarRepository.save(_event), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/calendar/{vehicle_id}/{calendar_id}")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deleteCalendar(@PathVariable("vehicle_id") long index, @PathVariable("calendar_id") long id) {
		try {
			Optional<Vehicle> vehicleData = vehicleRepository.findById(index);
			Vehicle vehicle = vehicleData.get();
			Optional<Calendar> calendarData = calendarRepository.findById(id);
			Calendar _calendar = calendarData.get();

			vehicle.getCalendar().remove(_calendar);

			calendarRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/calendar")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deleteAllCalendars() {
		try {
			calendarRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}

	@GetMapping("/calendar/place")
	public ResponseEntity<List<Calendar>> findByPlace(@RequestParam(required = false) String place) {
		try {
			List<Calendar> calendar = calendarRepository.findByPlace(place);

			if (calendar.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(calendar, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@GetMapping("/calendar/validateurl/{url}")
	public boolean isValidURL(@PathVariable("url") String url) {
            url = url.substring(1, url.length()-1);
            
	    try {
                URL _url = new URL (url);
	        URI uri = _url.toURI(); 
                return true;
	    } catch (MalformedURLException | URISyntaxException e) {
	        return false;
	    }

	    ///return true;
	}
}
