package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository gérant les opérations sur les événements olympiques.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}

