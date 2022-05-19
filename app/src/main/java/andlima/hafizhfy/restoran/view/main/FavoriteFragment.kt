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
import andlima.hafizhfy.restoran.local.datastore.UserManager
import andlima.hafizhfy.restoran.local.room.FavoriteFoodDatabase
import andlima.hafizhfy.restoran.view.main.adapter.AdapterFoodFavorite
import andlima.hafizhfy.restoran.viewmodel.ViewModelFavorite
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    // Get data store
    lateinit var userManager: UserManager

    // Get local room database
    private var mDb: FavoriteFoodDatabase? = null

    // Food adapter init
    lateinit var favFoodAdapter: AdapterFoodFavorite

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Get room database instance
        mDb = FavoriteFoodDatabase.getInstance(requireContext())

        userManager.id.asLiveData().observe(this, { userID ->
            btn_order.setOnClickListener {
                alertDialog(requireContext(), "Order", "Want to order it now?") {
                    val clearCart = mDb?.favoriteFoodDao()?.removeFromFavorite(userID.toInt())

                    if (clearCart != 0) {
                        snackbarLong(requireView(), "Food ordered")
                        Navigation.findNavController(view).navigate(R.id.action_favoriteFragment_to_homeFragment)
                    } else {
                        toast(requireContext(), "Order failed, try again later clearCart = $clearCart")
                    }
                }
            }
        })
    }

    private fun initViewModel(ownerID: Int) {
        val viewModel = ViewModelProvider(this).get(ViewModelFavorite::class.java)
        viewModel.getLiveDataFoodFavorite().observe(this, {
            if (it != null) {
                loading_content.visibility = View.GONE
                no_favorite_handler.visibility = View.GONE
                favFoodAdapter.setFavoriteFoodList(it)
                favFoodAdapter.notifyDataSetChanged()
            } else {
                no_favorite_handler.visibility = View.VISIBLE
                loading_content.visibility = View.GONE
                btn_order.visibility = View.GONE
            }
        })
        viewModel.getFavoriteFood(requireContext(), ownerID)
    }

    override fun onResume() {
        super.onResume()
        userManager = UserManager(requireContext())
        userManager.id.asLiveData().observe(this, { userID ->
            rv_favorite_food_list.layoutManager = LinearLayoutManager(requireContext())
            favFoodAdapter = AdapterFoodFavorite {
                val selectedData = bundleOf("SELECTED_DATA" to it)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_favoriteFragment_to_detailFragment, selectedData)
            }
            rv_favorite_food_list.adapter = favFoodAdapter

            initViewModel(userID.toInt())
        })
    }
}