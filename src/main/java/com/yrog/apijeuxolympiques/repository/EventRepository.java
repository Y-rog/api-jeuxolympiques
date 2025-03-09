package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.pojo.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository <Event, Long> {


}

