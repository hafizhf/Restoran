package andlima.hafizhfy.restoran.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteFoodDao {
    @Insert
    fun insertNewFavorite(favoriteFood: FavoriteFood) : Long

    @Query("SELECT * FROM FavoriteFood ORDER BY id DESC")
    fun getAllFavoriteFood() : List<FavoriteFood>

    @Query("SELECT * FROM FavoriteFood WHERE ownerId = :ownerId ORDER BY id DESC")
    fun getUsersFavoriteFood(ownerId : Int) : List<FavoriteFood>

    @Query("SELECT * FROM FavoriteFood WHERE nama_makanan = :nama_makanan AND ownerId = :ownerId")
    fun checkFoodAddedByUser(nama_makanan: String, ownerId: Int) : List<FavoriteFood>

    @Query("SELECT id FROM FavoriteFood WHERE nama_makanan = :nama_makanan AND ownerId = :ownerId")
    fun getFavoriteFoodID(nama_makanan: String, ownerId: Int) : Int

    @Delete
    fun deleteFood(favoriteFood: FavoriteFood) : Int

    @Query("DELETE FROM FavoriteFood WHERE id = :id")
    fun removeFromFavorite(id: Int) : Int

    @Query("DELETE FROM FavoriteFood WHERE nama_makanan = :nama_makanan")
    fun deleteSpecificFood(nama_makanan: String) : Int
}