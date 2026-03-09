package gatewayapi.business.offer;

import dto.offerapi.Offer;
import dto.offerapi.OfferInput;
import gatewayapi.repository.offer.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferBusiness {

    private final OfferRepository offerRepository;

    public OfferBusiness(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public List<Offer> getActiveOffers() {
        return offerRepository.findActive();
    }

    public Offer getOfferById(int id) {
        return offerRepository.findById(id);
    }

    public Offer createOffer(OfferInput offerInput) {
        return offerRepository.create(offerInput);
    }

    public Offer updateOffer(int id, OfferInput offerInput) {
        return offerRepository.update(id, offerInput);
    }

    public void deleteOffer(int id) {
        offerRepository.delete(id);
    }
}

