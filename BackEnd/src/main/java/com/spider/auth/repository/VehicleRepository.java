package com.spider.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spider.auth.models.Vehicle;;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	List<Vehicle> findByQualis(String qualis);
	List<Vehicle> findByNameContaining(String name);
	List<Vehicle> findByType(String qualis);
}
