package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.dto.stats.SalesByOfferDTO;
import com.yrog.apijeuxolympiques.enums.CartStatus;
import com.yrog.apijeuxolympiques.entity.Cart;
import com.yrog.apijeuxolympiques.entity.CartItem;
import com.yrog.apijeuxolympiques.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository gérant les opérations sur les articles du panier.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Retourne tous les articles d'un panier.
     */
    List<CartItem> findByCart(Cart cart);

    /**
     * Retourne tous les articles payés d'un utilisateur.
     */
    List<CartItem> findByCartUserIdAndCartStatus(Long userId, CartStatus cartStatus);

    /**
     * Vérifie si une offre a des tickets vendus.
     */
    boolean existsByOfferAndCart_TransactionUuidIsNotNull(Offer offerToUpdate);

    /**
     * Retourne le nombre de ventes par offre.
     */
    @Query("SELECT c.offer.offerId as offerId, COUNT(c) as salesCount FROM CartItem c GROUP BY c.offer.offerId")
    List<SalesByOfferDTO> countSalesByOffer();
}
