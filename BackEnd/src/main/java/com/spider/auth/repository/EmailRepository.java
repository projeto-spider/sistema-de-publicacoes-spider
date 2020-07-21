package com.spider.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spider.auth.models.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {

}
