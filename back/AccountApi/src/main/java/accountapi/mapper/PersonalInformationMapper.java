package accountapi.mapper;

import accountapi.entity.PersonalInformationEntity;
import dto.accountapi.PersonalInformation;
import dto.accountapi.PersonalInformationRegister;

public class PersonalInformationMapper {

    public static PersonalInformation toDto(PersonalInformationEntity entity) {
        if (entity == null) {
            return null;
        }

        PersonalInformation dto = new PersonalInformation();
        dto.setId(entity.getId());
        dto.setFirstname(entity.getFirstname());
        dto.setLastname(entity.getLastname());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());

        return dto;
    }

    public static PersonalInformationEntity toEntity(PersonalInformationRegister dto) {
        if (dto == null) {
            return null;
        }

        PersonalInformationEntity entity = new PersonalInformationEntity();
        // pas id car il est genere par la base de données
        entity.setFirstname(dto.getFirstname());
        entity.setLastname(dto.getLastname());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());

        return entity;
    }
}