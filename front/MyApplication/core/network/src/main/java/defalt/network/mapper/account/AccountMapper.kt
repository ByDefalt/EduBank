package defalt.network.mapper.account

import defalt.domain.entity.account.Account as AccountEntity
import defalt.network.api.account.model.Account as AccountDto

fun AccountDto.toEntity(): AccountEntity {
    return AccountEntity(
        id = this.id,
        personalInfoId = this.personalInfoId,
        roleId = this.roleId,
        state = this.state,
    )
}

fun List<AccountDto>.toEntity(): List<AccountEntity> = this.map { it.toEntity() }
