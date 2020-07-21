package com.spider.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spider.auth.models.Approved;

public interface ApprovedRepository extends JpaRepository<Approved, Long> {

}
