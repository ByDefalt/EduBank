package accountapi.business;

import accountapi.entity.PersonalInformationEntity;
import accountapi.exception.FunctionalException;
import accountapi.exception.NotFoundException;
import accountapi.mapper.PersonalInformationMapper;
import accountapi.repository.PersonalInformationRepository;
import dto.accountapi.PersonalInformation;
import dto.accountapi.PersonalInformationRegister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonalInformationBusiness {

    private PersonalInformationRepository personalInformationRepository;

    public PersonalInformationBusiness(PersonalInformationRepository personalInformationRepository) {
        this.personalInformationRepository = personalInformationRepository;
    }

    public List<PersonalInformation> getAllPersonalInformation() {
        List<PersonalInformationEntity> entities;
        try {
            entities = personalInformationRepository.findAll();
        } catch (Exception e) {
            throw new NotFoundException("404", "Impossible de récupérer les informations personnelles");
        }

        List<PersonalInformation> dtos = new ArrayList<>();
        for (PersonalInformationEntity entity : entities) {
            dtos.add(PersonalInformationMapper.toDto(entity));
        }
        return dtos;
    }

    public PersonalInformation getPersonalInformationById(Integer id) {
        PersonalInformationEntity entity;
        try {
            entity = personalInformationRepository.findById(id);
        } catch (Exception e) {
            throw new NotFoundException("404", "Information personnelle non trouvée avec l'ID : " + id);
        }
        if (entity == null) {
            throw new NotFoundException("404", "Information personnelle non trouvée avec l'ID : " + id);
        }
        return PersonalInformationMapper.toDto(entity);
    }

    public PersonalInformation createPersonalInformation(PersonalInformationRegister registerDto) {
        PersonalInformationEntity personalInfo = PersonalInformationMapper.toEntity(registerDto);
        PersonalInformationEntity created;
        try {
            created = personalInformationRepository.create(personalInfo);
        } catch (Exception e) {
            throw new FunctionalException("400", "La création des informations personnelles a échoué : " + e.getMessage());
        }
        if (created == null) {
            throw new FunctionalException("400", "Impossible de créer les informations personnelles");
        }
        return PersonalInformationMapper.toDto(created);
    }

    public boolean deletePersonalInformation(Integer id) {
        this.getPersonalInformationById(id);
        try {
            return personalInformationRepository.delete(id);
        } catch (Exception e) {
            throw new FunctionalException("400", "La suppression de l'information personnelle a échoué avec l'ID : " + id);
        }
    }

    public PersonalInformation updatePersonalInformation(Integer id, PersonalInformation dto) {
        this.getPersonalInformationById(id);
        PersonalInformationEntity updated = PersonalInformationMapper.toEntity(dto);
        PersonalInformationEntity result;
        try {
            result = personalInformationRepository.update(id, updated);
        } catch (Exception e) {
            throw new FunctionalException("400", "La mise à jour des informations personnelles a échoué : " + e.getMessage());
        }
        if (result == null) {
            throw new FunctionalException("400", "La mise à jour des informations personnelles a échoué");
        }
        return PersonalInformationMapper.toDto(result);
    }
}