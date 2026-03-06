package defalt.database

import androidx.room.Database
import androidx.room.RoomDatabase
import defalt.database.account.TokenDao
import defalt.database.account.TokenEntity

@Database(
    entities = [TokenEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao
}
