package com.yrog.apijeuxolympiques.dto.ticket;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TicketResponse {

    private Long cartItemId;
    private String qrCode;
    private BigDecimal priceAtPurchase;

    private String eventTitle;
    private String eventLocation;
    private LocalDateTime eventDateTime;

    private String offerCategoryTitle;
    private Integer placesPerOffer;
}
