Résumé
======

J'ai créé des entités domain pour refléter les DTOs présents dans `core/network`.

Emplacement des entités créées
-----------------------------
- `core/domain/src/main/java/defalt/domain/entity/account` — entités liées aux comptes (AccountEntity, AccountRegisterEntity, PersonalInformationEntity, PersonalInformationRegisterEntity, RoleEntity, TokenResponseEntity, Token/SignIn request entities)
- `core/domain/src/main/java/defalt/domain/entity/offer` — OfferEntity, OffersPostRequestEntity, OffersIdPutRequestEntity, OffersIdStatePatchRequestEntity
- `core/domain/src/main/java/defalt/domain/entity/bank` — BankAccountEntity, BankAccountDetailEntity, BankAccountParameterEntity, BankAccountCreateRequestEntity, TypeEntity, MessageResponseEntity
- `core/domain/src/main/java/defalt/domain/entity/operation` — OperationEntity, BeneficiaryEntity, OperationCancelResponseEntity

Notes
-----
- Les noms utilisent le suffixe `Entity` pour marquer leur rôle dans le domaine.
- Les enums réseau ont été reflétés dans le domaine (ex. `OfferState`, `BankState`, `OperationState`).
- J'ai conservé la nullabilité telle qu'elle apparaît dans les DTOs.

Prochaines étapes suggérées
---------------------------
1. Implémenter des mappers `toDomain()` / `toNetwork()` entre DTOs et Entities.
2. Ajouter des tests unitaires minimalistes pour vérifier la transformation (happy path + 1-2 cas nullables).
3. Réviser les enums si vous souhaitez centraliser les valeurs (par exemple, déplacer la logique `toString()`/`encode`/`decode` côté domain).

Si tu veux, je peux maintenant:
- Générer automatiquement les fonctions `toDomain()` dans `core/network` (extension functions) pour convertir les DTOs en Entities.
- Ou créer un package `defalt.domain.mapper` avec converters.

Dis-moi quelle option tu veux pour la suite : créer automatiquement les mappers ou seulement garder les entités pour l'instant.

