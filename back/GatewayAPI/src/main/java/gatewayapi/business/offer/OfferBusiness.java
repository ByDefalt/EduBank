package gatewayapi.business.offer;

import dto.accountapi.RoleEnum;
import dto.accountapi.TokenRequest;
import dto.accountapi.TokenResponse;
import dto.offerapi.Offer;
import dto.offerapi.OfferInput;
import gatewayapi.repository.account.AccountRepository;
import gatewayapi.repository.offer.OfferRepository;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferBusiness {

    private final AccountRepository accountRepository;
    private final OfferRepository offerRepository;

    public OfferBusiness(AccountRepository accountRepository, OfferRepository offerRepository) {
        this.accountRepository = accountRepository;
        this.offerRepository = offerRepository;
    }

    public List<Offer> getAllOffers(String token) {
        String jwt = token.replace("Bearer ", "");
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setJwt(jwt);
        try{
            TokenResponse tokenResp = accountRepository.validateToken(tokenRequest);
            if (tokenResp.getRole().equals(RoleEnum.ADMIN.name())){
                return offerRepository.findAll();
            }else{
                return offerRepository.findActive();
            }
        }catch(SecurityException e){
            return offerRepository.findActive();
        }


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

