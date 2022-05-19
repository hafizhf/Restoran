package andlima.hafizhfy.restoran.network

import andlima.hafizhfy.restoran.model.menurestaurant.GetAllMenuRestaurantItem
import andlima.hafizhfy.restoran.model.menurestaurant.PutFood
import andlima.hafizhfy.restoran.model.menurestaurant.RequestFood
import andlima.hafizhfy.restoran.model.user.GetAllUserItem
import andlima.hafizhfy.restoran.model.user.RequestUser
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("menu-restoran")
    fun getAllMenuRestaurant() : Call<List<GetAllMenuRestaurantItem>>

    // Add food
    @POST("menu-restoran")
    fun addFood(@Body request: RequestFood) : Call<GetAllMenuRestaurantItem>

    // Update food
    @PUT("menu-restoran/{id}")
    fun updateFood(
        @Path("id") id: Int,
        @Body request: PutFood
    ) : Call<GetAllMenuRestaurantItem>

    @GET("user")
    fun getUser(@Query("email") email : String) : Call<List<GetAllUserItem>>

    // Register service
    @POST("user")
    fun postUser(@Body request: RequestUser) : Call<GetAllUserItem>

}