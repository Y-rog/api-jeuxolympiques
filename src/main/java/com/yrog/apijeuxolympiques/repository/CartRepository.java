package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.enums.CartStatus;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

    Optional<Cart> findByUserAndStatus(User user, CartStatus cartStatus);

    List<Cart> findByItems_Offer_Event_EventId(Long eventId);

}
