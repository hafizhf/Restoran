package andlima.hafizhfy.restoran.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.restoran.R
import andlima.hafizhfy.restoran.func.alertDialog
import andlima.hafizhfy.restoran.func.toast
import andlima.hafizhfy.restoran.local.datastore.UserManager
import andlima.hafizhfy.restoran.view.main.adapter.AdapterFood
import andlima.hafizhfy.restoran.viewmodel.ViewModelHome
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    // Food adapter init
    lateinit var foodAdapter: AdapterFood

    // Used for double back to exit app
    private var doubleBackToExit = false

    // Get data store
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Check if user click back button twice
        doubleBackExit()

        userManager.username.asLiveData().observe(this, {
            tv_username.text = it.toString()
        })
        userManager.id.asLiveData().observe(this, { userID ->
            getCartAmount(userID.toInt())
        })

//        foodAdapter = AdapterFood {  }

        rv_food_list.layoutManager = LinearLayoutManager(requireContext())
        foodAdapter = AdapterFood {
            val selectedData = bundleOf("SELECTED_DATA" to it)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_detailFragment, selectedData)
        }
        rv_food_list.adapter = foodAdapter

        initViewModel()

        btn_goto_favorite.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_favoriteFragment)
        }

        btn_logout.setOnClickListener {
            alertDialog(requireContext(), "Logout", "Are you sure want to logout?") {
                GlobalScope.launch {
                    userManager.clearData()
                }

                Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_loginFragment)
                toast(requireContext(), "You are logged out")
            }
        }

        btn_add_new_menu.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addFoodFragment)
        }

    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this).get(ViewModelHome::class.java)
        viewModel.getLiveDataFood().observe(this, {
            if (it != null) {
                loading_content.visibility = View.GONE
                foodAdapter.setFoodList(it)
                foodAdapter.notifyDataSetChanged()
            } else {
                loading_content.visibility = View.GONE
                nothing_handler.visibility = View.VISIBLE
            }
        })
        viewModel.getFoodData()
    }

    private fun getCartAmount(ownerID: Int) {
        val viewModel = ViewModelProvider(this).get(ViewModelHome::class.java)
        viewModel.getLiveCartAmount().observe(this, {
            if (it != 0) {
                info_cart.visibility = View.VISIBLE
                tv_item_amount.text = it.toString()
            } else {
                info_cart.visibility = View.GONE
            }
        })
        viewModel.getCartItemAmount(requireContext(), ownerID)
    }

    // Function to exit app with double click on back button----------------------------------------
    private fun doubleBackExit() {
        activity?.onBackPressedDispatcher
            ?.addCallback(this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    if (doubleBackToExit) {
                        activity!!.finish()
                    } else {
                        doubleBackToExit = true
                        toast(requireContext(), "Press again to exit")

                        Handler(Looper.getMainLooper()).postDelayed(Runnable {
                            kotlin.run {
                                doubleBackToExit = false
                            }
                        }, 2000)
                    }
                }
            })
    }
}