package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.pojo.Offer;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> getAllOffersByEventEventId(Long eventId);

}

