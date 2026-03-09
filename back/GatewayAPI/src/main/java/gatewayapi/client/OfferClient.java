package gatewayapi.client;

import dto.offerapi.Offer;
import dto.offerapi.OfferInput;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface OfferClient {

    @RequestLine("GET /offers")
    @Headers("Content-Type: application/json")
    List<Offer> getAllOffers();

    @RequestLine("GET /offers/active")
    @Headers("Content-Type: application/json")
    List<Offer> getActiveOffers();

    @RequestLine("GET /offers/{id}")
    @Headers("Content-Type: application/json")
    Offer getOfferById(@Param("id") int id);

    @RequestLine("POST /offers")
    @Headers("Content-Type: application/json")
    Offer createOffer(OfferInput offerInput);

    @RequestLine("PUT /offers/{id}")
    @Headers("Content-Type: application/json")
    Offer updateOffer(@Param("id") int id, OfferInput offerInput);

    @RequestLine("DELETE /offers/{id}")
    @Headers("Content-Type: application/json")
    void deleteOffer(@Param("id") int id);
}

