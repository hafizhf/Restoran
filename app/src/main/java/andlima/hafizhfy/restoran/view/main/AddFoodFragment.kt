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
import andlima.hafizhfy.restoran.model.menurestaurant.RequestFood
import andlima.hafizhfy.restoran.network.ApiClient
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_add_food.*
import retrofit2.Call
import retrofit2.Response

class AddFoodFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add_menu.setOnClickListener {
            val name = et_newfood_name.text.toString()
            val price = et_newfood_price.text.toString()
            val desc = et_newfood_description.text.toString()

            if (name != "" || price != "" || desc != "") {
                addFood(name, price, desc)
            } else {
                toast(requireContext(), "Please input all field")
            }
        }
    }

    private fun addFood(name: String, price: String, desc: String) {
        ApiClient.instance.addFood(RequestFood(desc, price, name))
            .enqueue(object : retrofit2.Callback<GetAllMenuRestaurantItem>{
                override fun onResponse(
                    call: Call<GetAllMenuRestaurantItem>,
                    response: Response<GetAllMenuRestaurantItem>
                ) {
                    if (response.isSuccessful) {
                        snackbarLong(requireView(), "New menu added")
                        Navigation.findNavController(view!!)
                            .navigate(R.id.action_addFoodFragment_to_homeFragment)
                    } else {
                        alertDialog(
                            requireContext(),
                            "Add menu failed",
                            response.message() +"\n\nTry again"
                        ) {}
                    }
                }

                override fun onFailure(call: Call<GetAllMenuRestaurantItem>, t: Throwable) {
                    alertDialog(
                        requireContext(),
                        "Add menu error",
                        t.message +"\n\nTry again"
                    ) {}
                }

            })
    }
}