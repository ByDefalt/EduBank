package gatewayapi.repository.offer;

import dto.offerapi.Offer;
import dto.offerapi.OfferInput;
import gatewayapi.client.OfferClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferRepository {

    private final OfferClient offerClient;

    public OfferRepository(OfferClient offerClient) {
        this.offerClient = offerClient;
    }

    public List<Offer> findAll() {
        return offerClient.getAllOffers();
    }

    public List<Offer> findActive() {
        return offerClient.getActiveOffers();
    }

    public Offer findById(int id) {
        return offerClient.getOfferById(id);
    }

    public Offer create(OfferInput offerInput) {
        return offerClient.createOffer(offerInput);
    }

    public Offer update(int id, OfferInput offerInput) {
        return offerClient.updateOffer(id, offerInput);
    }

    public void delete(int id) {
        offerClient.deleteOffer(id);
    }
}

