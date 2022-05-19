package andlima.hafizhfy.restoran.viewmodel

import andlima.hafizhfy.restoran.local.room.FavoriteFood
import andlima.hafizhfy.restoran.local.room.FavoriteFoodDatabase
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelFavorite: ViewModel() {

    // Get local room database
    private var mDb: FavoriteFoodDatabase? = null

    lateinit var liveDataList: MutableLiveData<List<FavoriteFood>>

    init {
        liveDataList = MutableLiveData()
    }

    fun getLiveDataFoodFavorite() : MutableLiveData<List<FavoriteFood>> {
        return liveDataList
    }

    fun getFavoriteFood(context: Context, ownerID: Int) {
        // Get room database instance
        mDb = FavoriteFoodDatabase.getInstance(context)

        val foods = mDb?.favoriteFoodDao()?.getUsersFavoriteFood(ownerID)

        if (foods?.size != 0) {
            liveDataList.postValue(foods)
        } else {
            liveDataList.postValue(null)
        }
    }
}