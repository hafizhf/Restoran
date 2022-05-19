package andlima.hafizhfy.restoran.viewmodel

import andlima.hafizhfy.restoran.local.room.FavoriteFoodDatabase
import andlima.hafizhfy.restoran.model.menurestaurant.GetAllMenuRestaurantItem
import andlima.hafizhfy.restoran.network.ApiClient
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response

class ViewModelHome : ViewModel() {

    // Get local room database
    private var mDb: FavoriteFoodDatabase? = null

    lateinit var liveDataList: MutableLiveData<List<GetAllMenuRestaurantItem>>
    lateinit var liveDataCartAmount: MutableLiveData<Int>

    init {
        liveDataList = MutableLiveData()
        liveDataCartAmount = MutableLiveData()
    }

    fun getLiveDataFood() : MutableLiveData<List<GetAllMenuRestaurantItem>> {
        return liveDataList
    }

    fun getLiveCartAmount() : MutableLiveData<Int> {
        return liveDataCartAmount
    }

    fun getFoodData() {
        ApiClient.instance.getAllMenuRestaurant()
            .enqueue(object : retrofit2.Callback<List<GetAllMenuRestaurantItem>>{
                override fun onResponse(
                    call: Call<List<GetAllMenuRestaurantItem>>,
                    response: Response<List<GetAllMenuRestaurantItem>>
                ) {
                    if (response.isSuccessful) {
                        liveDataList.postValue(response.body())
                    } else {
                        liveDataList.postValue(null)
                    }
                }

                override fun onFailure(call: Call<List<GetAllMenuRestaurantItem>>, t: Throwable) {
                    liveDataList.postValue(null)
                }

            })
    }

    fun getCartItemAmount(context: Context, ownerID: Int) {
        // Get room database instance
        mDb = FavoriteFoodDatabase.getInstance(context)

        val amount = mDb?.favoriteFoodDao()?.getUsersFavoriteFood(ownerID)

        liveDataCartAmount.postValue(amount?.size)
    }
}