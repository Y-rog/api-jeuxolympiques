package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.dto.cartItem.SalesByOfferDTO;
import com.yrog.apijeuxolympiques.enums.CartStatus;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.pojo.CartItem;
import com.yrog.apijeuxolympiques.pojo.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

    List<CartItem> findByCartUserIdAndCartStatus(Long userId, CartStatus cartStatus);

    boolean existsByOfferAndCart_TransactionUuidIsNotNull(Offer offerToUpdate);

    @Query("SELECT c.offer.offerId as offerId, COUNT(c) as salesCount FROM CartItem c GROUP BY c.offer.offerId")
    List<SalesByOfferDTO> countSalesByOffer();

}
