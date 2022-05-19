package andlima.hafizhfy.restoran.viewmodel

import andlima.hafizhfy.restoran.model.menurestaurant.GetAllMenuRestaurantItem
import andlima.hafizhfy.restoran.network.ApiClient
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response

class ViewModelHome : ViewModel() {

    lateinit var liveDataList: MutableLiveData<List<GetAllMenuRestaurantItem>>

    init {
        liveDataList = MutableLiveData()
    }

    fun getLiveDataFood() : MutableLiveData<List<GetAllMenuRestaurantItem>> {
        return liveDataList
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
}