package andlima.hafizhfy.restoran.view.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.restoran.R
import andlima.hafizhfy.restoran.func.*
import andlima.hafizhfy.restoran.local.datastore.UserManager
import andlima.hafizhfy.restoran.model.user.GetAllUserItem
import andlima.hafizhfy.restoran.network.ApiClient
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LoginFragment : Fragment() {

    // Used for double back to exit app
    private var doubleBackToExit = false

    // Get data store
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if user already logged in
        isLoggedIn()

        // Get something from data store
        userManager = UserManager(requireContext())

        // Check if user click back button twice
        doubleBackExit()

        // Show password
        btn_show_pwd.setOnClickListener {
            showPassword(et_password, btn_show_pwd)
        }

        btn_login.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            hideAllPopUp(cv_email_popup, cv_password_popup)

            if (email != "" && password != "") {
                loading_login.visibility = View.VISIBLE
                loginAuth(email, password)
            } else {
                toast(requireContext(), "Please input all field")
            }
        }

        btn_goto_register.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun loginAuth(email: String, password: String) {
        ApiClient.instance.getUser(email)
            .enqueue(object : retrofit2.Callback<List<GetAllUserItem>>{
                override fun onResponse(
                    call: Call<List<GetAllUserItem>>,
                    response: Response<List<GetAllUserItem>>
                ) {
                    loading_login.visibility = View.GONE
                    if (response.isSuccessful) {
                        if (response.body()?.isEmpty() == true) {
                            toast(requireContext(), "Unknown User")
                        } else {
                            when {
                                response.body()?.size!! > 1 -> {
                                    toast(requireContext(), "Please input your data correctly")
                                }
                                email != response.body()!![0].email -> {
                                    showPopUp(cv_email_popup, tv_email_popup, "Email not registered")
                                }
                                password != response.body()!![0].password -> {
                                    showPopUp(cv_password_popup, tv_password_popup, "Wrong password")
                                }
                                else -> {
                                    GlobalScope.launch {
                                        userManager.loginUser(
                                            response.body()!![0].id,
                                            response.body()!![0].username,
                                            response.body()!![0].email,
                                            response.body()!![0].password
                                        )
                                    }

                                    Navigation.findNavController(view!!)
                                        .navigate(R.id.action_loginFragment_to_homeFragment)
                                }
                            }
                        }
                    } else {
                        alertDialog(requireContext(), "Login failed", response.message()
                                +"\n\nTry again") {}
                    }
                }

                override fun onFailure(call: Call<List<GetAllUserItem>>, t: Throwable) {
                    loading_login.visibility = View.GONE
                    alertDialog(requireContext(), "Login error", "${t.message}") {}
                }

            })
    }

    private fun isLoggedIn() {
        // Get something from data store
        userManager = UserManager(requireContext())

        userManager.email.asLiveData().observe(this, {
            if (it != "") {
                Navigation.findNavController(view!!)
                    .navigate(R.id.action_loginFragment_to_homeFragment)
            }
        })
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