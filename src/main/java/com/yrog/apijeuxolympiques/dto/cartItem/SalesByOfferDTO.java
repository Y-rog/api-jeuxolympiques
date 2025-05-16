package com.yrog.apijeuxolympiques.dto.cartItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public interface SalesByOfferDTO {

    Long getOfferId();

    Long getSalesCount();


}
