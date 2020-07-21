package com.spider.auth.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.spider.auth.models.Email;
import com.spider.auth.repository.EmailRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class EmailController {
	@Autowired
	EmailRepository emailRepository;
	
	@GetMapping("/email")
	public ResponseEntity<List<Email>> getAllEmails(@RequestParam(required = false) String name) {
		try {
			List<Email> email = new ArrayList<Email>();

			if (name == null)
				emailRepository.findAll().forEach(email::add);

			if (email.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(email, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/email/{email_id}")
	public ResponseEntity<Email> getEmailById(@PathVariable("email_id") long id) {
		Optional<Email> emailData = emailRepository.findById(id);

		if (emailData.isPresent()) {
			return new ResponseEntity<>(emailData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/email")
//	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Email> createEmail(@RequestBody Email email) {
		try {
			Email _email = emailRepository
					.save(new Email(email.getEmail(), email.getPassword(), email.getHost(), email.getPort()));
			return new ResponseEntity<>(_email, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/email/{email_id}")
//	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Email> updateEmail(@PathVariable("email_id") long id, @RequestBody Email email) {
		Optional<Email> emailData = emailRepository.findById(id);

		if (emailData.isPresent()) {
			Email _email = emailData.get();
			_email.setEmail(email.getEmail());
			_email.setPassword(email.getPassword());
			_email.setHost(email.getHost());
			_email.setPort(email.getPort());
			return new ResponseEntity<>(emailRepository.save(_email), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/email/{email_id}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deleteEmail(@PathVariable("email_id") long id) {
		try {
			emailRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/email")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deleteAllEmails() {
		try {
			emailRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}
}
