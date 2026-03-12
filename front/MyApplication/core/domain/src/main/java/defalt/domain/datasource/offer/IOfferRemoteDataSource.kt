package defalt.domain.datasource.offer

import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OfferInput
import defalt.utils.NetworkResult

interface IOfferRemoteDataSource {



    /**
     * Récupère uniquement les offres dont l'état est "active".
     * Correspond à GET offers/active
     */
    suspend fun getActiveOffers(): NetworkResult<List<Offer>>

    /**
     * Récupère la liste des offres avec filtres optionnels.
     * Correspond à GET offers
     * @param state Filtre par état (active, inactive, expired)
     * @param activeOnly Filtre pour n'avoir que les offres actives
     */
    suspend fun getOffers(): NetworkResult<List<Offer>>

    /**
     * Récupère le détail d'une offre spécifique par son ID.
     * Correspond à GET offers/{id}
     */
    suspend fun getOfferById(id: Int): NetworkResult<Offer>



    /**
     * Crée une nouvelle offre.
     * Correspond à POST offers
     */
    suspend fun createOffer(offerInput: OfferInput): NetworkResult<Offer>

    /**
     * Met à jour une offre existante.
     * Correspond à PUT offers/{id}
     */
    suspend fun updateOffer(id: Int, offerInput: OfferInput): NetworkResult<Offer>

    /**
     * Supprime une offre.
     * Correspond à DELETE offers/{id}
     */
    suspend fun deleteOffer(id: Int): NetworkResult<Unit>
}
