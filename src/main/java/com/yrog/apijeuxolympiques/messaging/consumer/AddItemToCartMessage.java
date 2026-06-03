package com.yrog.apijeuxolympiques.messaging.consumer;

/**
 * Record représentant un message RabbitMQ pour l'ajout d'un article au panier.
 *
 * @param cartId  l'identifiant du panier
 * @param offerId l'identifiant de l'offre à ajouter
 */
public record AddItemToCartMessage(Long cartId, Long offerId) {}