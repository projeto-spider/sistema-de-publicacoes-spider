package com.spider.auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.spider.auth.models.Publication;
import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
	//List<User> findByAccess(String access);
	List<Publication> findPublicationByNome(String nome);
}