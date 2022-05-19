package andlima.hafizhfy.restoran.view.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.restoran.R
import andlima.hafizhfy.restoran.func.*
import andlima.hafizhfy.restoran.model.user.GetAllUserItem
import andlima.hafizhfy.restoran.model.user.RequestUser
import andlima.hafizhfy.restoran.network.ApiClient
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Response

class RegisterFragment : Fragment() {

    var sameEmailDetected : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Action for show password on password edittext
        btn_show_new_pwd.setOnClickListener {
            showPassword(et_new_password, btn_show_new_pwd)
        }

        // Action for show password on re-enter password edittext
        btn_show_new_repwd.setOnClickListener {
            showPassword(et_reenter_password, btn_show_new_repwd)
        }

        btn_register.setOnClickListener {
            hidePopUp(cv_name_popup)
            hidePopUp(cv_new_email_popup)
            hidePopUp(cv_re_pwd_popup)

            // Get data from edit text register form
            val username = et_new_name.text.toString()
            val email = et_new_email.text.toString()
            val password = et_new_password.text.toString()
            val repassword = et_reenter_password.text.toString()

            if (username != "" && email != "" && password != "" && repassword !="") {
                // Check if email registered
                loading_register.visibility = View.VISIBLE
                checkIfEmailUsed(email)

                // Do delay because of checking used email in network need more time than the program
                Handler(Looper.getMainLooper()).postDelayed({
                    // Do register
                    register(email, password, repassword, username)
                }, 2000)
            } else {
                toast(requireContext(), "Please input all field")
            }
        }

        btn_goto_login.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun checkIfEmailUsed(email: String) {
        ApiClient.instance.getUser(email)
            .enqueue(object : retrofit2.Callback<List<GetAllUserItem>>{
                override fun onResponse(
                    call: Call<List<GetAllUserItem>>,
                    response: Response<List<GetAllUserItem>>
                ) {
                    sameEmailDetected = if (response.isSuccessful) {
                        response.body()!!.size
                    } else {
                        0 // This should be illegal, but there's something wrong so...
//                        -1
                    }
                }

                override fun onFailure(call: Call<List<GetAllUserItem>>, t: Throwable) {
                    sameEmailDetected = -2
                }

            })
    }

    private fun register(email: String, password: String, repassword: String, username: String) {
        // Hide loading on registering
        loading_register.visibility = View.GONE

        // Check how much registered email that same with input

        val detected = sameEmailDetected

        // Main action
        when {
            detected < 0 -> {
                alertDialog(
                    requireContext(),
                    "Register failed",
                    "Something is wrong, please try again"
                ) {}
            }
            detected > 0 -> {
                showPopUp(
                    cv_new_email_popup,
                    tv_new_email_popup,
                    "Email already used"
                )
            }
            repassword != password -> {
                showPopUp(
                    cv_re_pwd_popup,
                    tv_re_pwd_popup,
                    "Re-enter password not match"
                )
            }
            else -> {
                ApiClient.instance.postUser(RequestUser(email, repassword, username))
                    .enqueue(object : retrofit2.Callback<GetAllUserItem>{
                        override fun onResponse(
                            call: Call<GetAllUserItem>,
                            response: Response<GetAllUserItem>
                        ) {
                            if (response.isSuccessful) {
                                snackbarLong(requireView(), "Register success")
                                Navigation.findNavController(view!!)
                                    .navigate(R.id.action_registerFragment_to_loginFragment)
                            } else {
                                alertDialog(
                                    requireContext(),
                                    "Register failed",
                                    response.message() +"\n\nTry again"
                                ) {}
                            }
                        }

                        override fun onFailure(call: Call<GetAllUserItem>, t: Throwable) {
                            alertDialog(
                                requireContext(),
                                "Register error",
                                "${t.message}"
                            ) {}
                        }
                    })
            }
        }
    }
}