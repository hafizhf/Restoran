package andlima.hafizhfy.restoran.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteFood::class],
    version = 1
)
abstract class FavoriteFoodDatabase : RoomDatabase() {

    abstract fun favoriteFoodDao() : FavoriteFoodDao

    companion object {
        private var INSTANCE : FavoriteFoodDatabase? = null
        fun getInstance(context: Context) : FavoriteFoodDatabase? {
            synchronized(FavoriteFoodDatabase::class.java) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    FavoriteFoodDatabase::class.java, "FavoriteFood.db")
                    .allowMainThreadQueries().build()
            }
            return INSTANCE
        }
    }

    fun destroyInstance() {
        INSTANCE = null
    }
}