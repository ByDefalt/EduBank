package accountapi.mapper;

import accountapi.entity.RoleEntity;
import dto.accountapi.Role;

public class RoleMapper {

    public static Role toDto(RoleEntity entity) {
        if (entity == null) {
            return null;
        }

        Role dto = new Role();
        // on ne renvoie pas l'id dans le dto
        dto.setName(entity.getName());

        return dto;
    }

    public static RoleEntity toEntity(Role dto) {
        if (dto == null) {
            return null;
        }

        RoleEntity entity = new RoleEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());

        return entity;
    }
}