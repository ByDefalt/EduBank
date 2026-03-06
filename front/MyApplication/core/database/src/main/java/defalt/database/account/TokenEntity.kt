package defalt.database.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token")
data class TokenEntity(
    @PrimaryKey val id: Int = 0, // singleton : une seule ligne possible
    val jwt: String,
)
