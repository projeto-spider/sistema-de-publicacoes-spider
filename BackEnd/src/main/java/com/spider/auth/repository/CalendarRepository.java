package com.spider.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spider.auth.models.Calendar;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
	List<Calendar> findByStartDate(String startDate);
	List<Calendar> findByEndDate(String endDate);
	
//	methods for name, place or deadline for the events
//	List<Calendar> findByTitle(String title);
	List<Calendar> findByPlace(String place);
	List<Calendar> findByStartReminded(boolean startReminded);
	List<Calendar> findByDeadlineReminded(boolean deadlineReminded);
}
