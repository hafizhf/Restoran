package andlima.hafizhfy.restoran.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.restoran.R
import andlima.hafizhfy.restoran.func.alertDialog
import andlima.hafizhfy.restoran.func.snackbarCustom
import andlima.hafizhfy.restoran.func.snackbarLong
import andlima.hafizhfy.restoran.func.toast
import andlima.hafizhfy.restoran.local.datastore.UserManager
import andlima.hafizhfy.restoran.local.room.FavoriteFood
import andlima.hafizhfy.restoran.local.room.FavoriteFoodDatabase
import andlima.hafizhfy.restoran.model.menurestaurant.GetAllMenuRestaurantItem
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.os.bundleOf
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    // Get data store
    lateinit var userManager: UserManager

    // Get local room database
    private var mDb: FavoriteFoodDatabase? = null

    // Init floating action button clicked
    private var fabAdded : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get data from recyclerview selected item
        val selectedData = arguments?.getParcelable<GetAllMenuRestaurantItem>("SELECTED_DATA")
                as GetAllMenuRestaurantItem

        // Get something from data store
        userManager = UserManager(requireContext())

        // Get room database instance
        mDb = FavoriteFoodDatabase.getInstance(requireContext())

        userManager.id.asLiveData().observe(this, { userID ->
            val data = mDb?.favoriteFoodDao()?.checkFoodAddedByUser(
                selectedData.namaMakanan,
                userID.toInt()
            )

            fabAdded = data?.size != 0

            if (fabAdded) {
                changeFabIcon(R.drawable.ic_remove)
                changeFabColor(R.color.main_remove)
            } else {
                changeFabIcon(R.drawable.ic_add_item)
                changeFabColor(R.color.main_available)
            }

            fab_add_item.setOnClickListener {
                if (!fabAdded) {
                    // Add food to cart
                    val selectedFood = FavoriteFood(
                        null,
                        selectedData.namaMakanan,
                        selectedData.harga,
                        selectedData.gambar,
                        selectedData.deskripsi,
                        "1",
                        userID
                    )

                    val addToCart = mDb?.favoriteFoodDao()?.insertNewFavorite(selectedFood)

                    if (addToCart != 0.toLong()) {
                        changeFabIcon(R.drawable.ic_remove)
                        snackbarCustom(
                            requireView(),
                            "Added to cart",
                            "See Cart",
                        ) {
                            Navigation.findNavController(view)
                                .navigate(R.id.action_detailFragment_to_favoriteFragment)
                        }
                        fabAdded = true
                    } else {
                        toast(requireContext(), "Add to cart failed")
                    }
                } else {
                    GlobalScope.launch {
                        val foodID = mDb?.favoriteFoodDao()?.getFavoriteFoodID(
                            selectedData.namaMakanan,
                            userID.toInt()
                        )

                        val removeFromCart = mDb?.favoriteFoodDao()?.removeFromFavorite(foodID!!)

                        requireActivity().runOnUiThread {
                            if (removeFromCart != 0) {
                                changeFabIcon(R.drawable.ic_add_item)
                                snackbarLong(requireView(), "Removed from cart")
                                fabAdded = false
                            } else {
                                toast(requireContext(), "Failed to remove from cart")
                            }
                        }
                    }
                }
            }

        })

        // Show selected data on view
        Glide.with(requireContext()).load(selectedData.gambar).into(iv_thumbnail_makanan_detail)
        tv_nama_makanan_detail.text = selectedData.namaMakanan
        tv_harga_detail.append(selectedData.harga)
        tv_deskripsi_detail.text = selectedData.deskripsi

        fab_edit_food.setOnClickListener {
            val thisData = bundleOf("THIS_DATA" to selectedData)
            Navigation.findNavController(view)
                .navigate(R.id.action_detailFragment_to_editFoodFragment, thisData)
        }
    }

    private fun changeFabIcon(icon: Int) {
        fab_add_item.setImageResource(icon)
    }

    private fun changeFabColor(color: Int) {
        fab_add_item.backgroundTintList = ColorStateList.valueOf(color)
        fab_add_item.rippleColor = color
    }
}