package defalt.database.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(token: TokenEntity)

    @Query("DELETE FROM token")
    suspend fun delete()

    @Query("SELECT * FROM token WHERE id = 0 LIMIT 1")
    suspend fun get(): TokenEntity?
}
