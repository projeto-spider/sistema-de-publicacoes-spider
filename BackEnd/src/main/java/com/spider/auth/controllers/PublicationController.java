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

import com.spider.auth.models.Publication;
import com.spider.auth.repository.PublicationRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class PublicationController {
	@Autowired
	PublicationRepository publicationRepository;

	  @GetMapping("/publications")
	  public ResponseEntity<List<Publication>> getAllPublications(@RequestParam(required = false) String name) {
	    try {
	      List<Publication> publications = new ArrayList<Publication>();

	      if (name == null)
	    	  publicationRepository.findAll().forEach(publications::add);
	      else
	    	  publicationRepository.findPublicationByNome(name).forEach(publications::add);

	      if (publications.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }

	      return new ResponseEntity<>(publications, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	  
	  @GetMapping("/publications/{publication_id}")
	  public ResponseEntity<Publication> getPublicationById(@PathVariable("publication_id") long id) {
	    Optional<Publication> publicationData = publicationRepository.findById(id);

	    if (publicationData.isPresent()) {
	      return new ResponseEntity<>(publicationData.get(), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  @PostMapping("/publications")
	  @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	  public ResponseEntity<Publication> createPublication(@RequestBody Publication publication) {
	    try {
	    	Publication _publication = publicationRepository
	          .save(new Publication(publication.getQualis(), publication.getNome(), publication.getAutor(), publication.getNomeveiculo(),publication.getYear(), publication.getSummary(), publication.getKeyword(), publication.getResearchline(), publication.getEventlink(), publication.getType()));
	      return new ResponseEntity<>(_publication, HttpStatus.CREATED);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
	    }
	  }

	  @PutMapping("/publications/{publication_id}")
	  @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	  public ResponseEntity<Publication> updatePublication(@PathVariable("publication_id") long id, @RequestBody Publication publication) {
	    Optional<Publication> publicationData = publicationRepository.findById(id);

	    if (publicationData.isPresent()) {
	    	Publication _publication = publicationData.get();
	    	_publication.setQualis(publication.getQualis());
                _publication.setNome(publication.getNome());
                _publication.setAutor(publication.getAutor());
                _publication.setNomeveiculo(publication.getNomeveiculo());
                _publication.setYear(publication.getYear());
                _publication.setSummary(publication.getSummary());
                _publication.setKeyword(publication.getKeyword());
                _publication.setResearchline(publication.getResearchline());
                _publication.setEventlink(publication.getEventlink());
                _publication.setType(publication.getType());
	      
	      return new ResponseEntity<>(publicationRepository.save(_publication), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  @DeleteMapping("/publications/{publication_id}")
	  @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	  public ResponseEntity<HttpStatus> deletePublication(@PathVariable("publication_id") long id) {
	    try {
	    	publicationRepository.deleteById(id);
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	    }
	  }

	  @DeleteMapping("/publications")
	  @PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<HttpStatus> deleteAllUsers() {
	    try {
	    	publicationRepository.deleteAll();
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	    }

	  }

	  @GetMapping("/publications/nome")
	  public ResponseEntity<List<Publication>> findByNome(@RequestParam(required = true) String nome) {
	    try {
	      List<Publication> publications = publicationRepository.findPublicationByNome(nome);

	      if (publications.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }
	      return new ResponseEntity<>(publications, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	    }
	  }
}
