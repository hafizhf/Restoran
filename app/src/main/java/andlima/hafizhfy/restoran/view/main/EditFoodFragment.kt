package andlima.hafizhfy.restoran.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.restoran.R
import andlima.hafizhfy.restoran.func.alertDialog
import andlima.hafizhfy.restoran.func.snackbarLong
import andlima.hafizhfy.restoran.func.toast
import andlima.hafizhfy.restoran.model.menurestaurant.GetAllMenuRestaurantItem
import andlima.hafizhfy.restoran.model.menurestaurant.PutFood
import andlima.hafizhfy.restoran.network.ApiClient
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_edit_food.*
import retrofit2.Call
import retrofit2.Response

class EditFoodFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get data from detail selected item
        val selectedData = arguments?.getParcelable<GetAllMenuRestaurantItem>("THIS_DATA")
                as GetAllMenuRestaurantItem

        et_edit_name.setText(selectedData.namaMakanan)
        et_edit_price.setText(selectedData.harga)
        et_edit_thumbnail.setText(selectedData.gambar)
        et_edit_description.setText(selectedData.deskripsi)

        btn_save_food.setOnClickListener {
            val newName = et_edit_name.text.toString()
            val newPrice = et_edit_price.text.toString()
            val newImage = et_edit_thumbnail.text.toString()
            val newDesc = et_edit_description.text.toString()

            if (newName != "" || newPrice != "" || newImage != "" || newDesc != "") {
                updateFood(selectedData.id, newName, newPrice, newImage, newDesc)
            } else {
                toast(requireContext(), "Don't leave any blank form")
            }
        }

        btn_delete_food.setOnClickListener {
            alertDialog(requireContext(), "Delete Food", "Are you sure want to delete this food?") {
                deleteFood(selectedData.id)
            }
        }
    }

    private fun updateFood(
        id: String,
        newName: String,
        newPrice: String,
        newImage: String,
        newDesc: String
    ) {
        ApiClient.instance
            .updateFood(id.toInt(), PutFood(
                newDesc,
                newImage,
                newPrice,
                newName
            ))
            .enqueue(object : retrofit2.Callback<GetAllMenuRestaurantItem>{
                override fun onResponse(
                    call: Call<GetAllMenuRestaurantItem>,
                    response: Response<GetAllMenuRestaurantItem>
                ) {
                    if (response.isSuccessful) {
                        snackbarLong(requireView(), "Food data updated")
                        Navigation.findNavController(view!!)
                            .navigate(R.id.action_editFoodFragment_to_homeFragment)
                    } else {
                        alertDialog(
                            requireContext(),
                            "Update food failed",
                            response.message() + "\n\nTry again"
                        ) {}
                    }
                }

                override fun onFailure(call: Call<GetAllMenuRestaurantItem>, t: Throwable) {
                    alertDialog(
                        requireContext(),
                        "Update food error",
                        t.message + "\n\nTry again"
                    ) {}
                }

            })
    }

    private fun deleteFood(foodID: String) {
        ApiClient.instance.deleteFood(foodID.toInt())
            .enqueue(object : retrofit2.Callback<GetAllMenuRestaurantItem>{
                override fun onResponse(
                    call: Call<GetAllMenuRestaurantItem>,
                    response: Response<GetAllMenuRestaurantItem>
                ) {
                    if (response.isSuccessful) {
                        snackbarLong(requireView(), "Food deleted from menu")
                        Navigation.findNavController(view!!)
                            .navigate(R.id.action_editFoodFragment_to_homeFragment)
                    } else {
                        alertDialog(
                            requireContext(),
                            "Delete food failed",
                            response.message() + "\n\nTry again"
                        ) {}
                    }
                }

                override fun onFailure(call: Call<GetAllMenuRestaurantItem>, t: Throwable) {
                    alertDialog(
                        requireContext(),
                        "Delete food error",
                        t.message + "\n\nTry again"
                    ) {}
                }

            })
    }
}