package com.yrog.apijeuxolympiques.messaging.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.pojo.CartItem;
import com.yrog.apijeuxolympiques.pojo.Offer;
import com.yrog.apijeuxolympiques.repository.CartRepository;
import com.yrog.apijeuxolympiques.repository.CartItemRepository;
import com.yrog.apijeuxolympiques.repository.OfferRepository;
import com.yrog.apijeuxolympiques.service.OfferService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.Duration;

@Service
public class CartItemConsumer {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferService offerService;

    @Value("${cart.item.item_expiration_duration}")
    private Long itemExpirationDuration;



    @RabbitListener(queues = "cartItemQueue")
    @Transactional
    public void handleAddItemToCart(Map<String, Object> message) {
        try {
            Number cartIdNumber = (Number) message.get("cartId");
            if (cartIdNumber == null) {
                System.out.println("Erreur : cartId est manquant dans le message.");
                return;
            }
            Long cartId = cartIdNumber.longValue();

            Number offerIdNumber = (Number) message.get("offerId");
            if (offerIdNumber == null) {
                System.out.println("Erreur : offerId est manquant dans le message.");
                return;
            }
            Long offerId = offerIdNumber.longValue();

            Object priceObj = message.get("priceAtPurchase");
            if (priceObj == null) {
                System.out.println("Erreur : priceAtPurchase est manquant dans le message.");
                return;
            }
            BigDecimal priceAtPurchase = new BigDecimal(priceObj.toString());

            Optional<Cart> cartOptional = cartRepository.findById(cartId);
            if (cartOptional.isEmpty()) {
                System.out.println("Erreur : Panier non trouvé pour l'ID : " + cartId);
                return;
            }
            Cart cart = cartOptional.get();

            Offer offer = offerRepository.findById(offerId)
                    .orElseThrow(() -> new RuntimeException("Offer not found for offerId: " + offerId));

            if (!offerService.checkAvailabilityForOffer(offer.getOfferId(), 1)) {
                System.out.println("Erreur : Pas assez de places disponibles pour l'offre " + offerId);
                return;
            }

            CartItem item = new CartItem();
            item.setCart(cart);
            item.setOffer(offer);
            item.setPriceAtPurchase(priceAtPurchase);
            item.setQrCode("");
            item.setExpirationTime(Instant.now().plus(Duration.ofMinutes(itemExpirationDuration)));

            cart.getItems().add(item);
            cart.setUpdatedAt(java.time.LocalDateTime.now());

            cart.setAmount(cart.getItems().stream()
                    .map(CartItem::getPriceAtPurchase)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            cartRepository.save(cart);

            System.out.println("✅ Article ajouté au panier avec succès.");

        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout au panier : " + e.getMessage());
        }
    }


}


