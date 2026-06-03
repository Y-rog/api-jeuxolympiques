package com.yrog.apijeuxolympiques.messaging.consumer;

import com.yrog.apijeuxolympiques.entity.Cart;
import com.yrog.apijeuxolympiques.entity.CartItem;
import com.yrog.apijeuxolympiques.entity.Offer;
import com.yrog.apijeuxolympiques.repository.CartRepository;
import com.yrog.apijeuxolympiques.repository.OfferRepository;
import com.yrog.apijeuxolympiques.service.OfferService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

/**
 * Consumer RabbitMQ gérant l'ajout d'articles au panier de façon asynchrone.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemConsumer {

    private final CartRepository cartRepository;
    private final OfferRepository offerRepository;
    private final OfferService offerService;

    @Value("${cart.item.item_expiration_duration}")
    private Long itemExpirationDuration;

    /**
     * Traite un message RabbitMQ pour ajouter un article au panier.
     *
     * @param message le message contenant cartId et offerId
     */
    @RabbitListener(queues = "cartItemQueue")
    @Transactional
    public void handleAddItemToCart(AddItemToCartMessage message) {
        log.info("Message reçu pour ajout panier : cartId={}, offerId={}",
                message.cartId(), message.offerId());

        Cart cart = cartRepository.findById(message.cartId())
                .orElseThrow(() -> new EntityNotFoundException("Panier introuvable : " + message.cartId()));

        Offer offer = offerRepository.findById(message.offerId())
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable : " + message.offerId()));

        if (!offerService.checkAvailabilityForOffer(offer.getOfferId(), 1)) {
            throw new IllegalStateException("Pas assez de places disponibles pour l'offre : " + message.offerId());
        }

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setOffer(offer);
        item.setPriceAtPurchase(offer.getPrice());
        item.setQrCode("");
        item.setExpirationTime(Instant.now().plus(Duration.ofMinutes(itemExpirationDuration)));

        cart.getItems().add(item);
        cart.setAmount(cart.getItems().stream()
                .map(CartItem::getPriceAtPurchase)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        cartRepository.save(cart);
        log.info("Article ajouté avec succès au panier {}", message.cartId());
    }
}


