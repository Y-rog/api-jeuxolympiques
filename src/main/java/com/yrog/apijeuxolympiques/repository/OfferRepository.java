package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.pojo.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}

