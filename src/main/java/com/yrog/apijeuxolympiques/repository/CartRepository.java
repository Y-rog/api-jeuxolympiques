package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.entity.Cart;
import com.yrog.apijeuxolympiques.entity.User;
import com.yrog.apijeuxolympiques.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository gérant les opérations sur les paniers.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Retourne le panier d'un utilisateur.
     */
    Optional<Cart> findByUser(User user);

    /**
     * Retourne le panier d'un utilisateur par statut.
     */
    Optional<Cart> findByUserAndStatus(User user, CartStatus cartStatus);

    /**
     * Retourne tous les paniers contenant des offres liées à un événement.
     */
    List<Cart> findByItems_Offer_Event_EventId(Long eventId);

    /**
     * Retourne un panier avec ses articles chargés.
     */
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.cartId = :cartId")
    Optional<Cart> findByIdWithItems(@Param("cartId") Long cartId);

    /**
     * Retourne un panier par son identifiant.
     */
    @Query("SELECT c FROM Cart c WHERE c.cartId = :cartId")
    Optional<Cart> findCartById(@Param("cartId") Long cartId);
}
