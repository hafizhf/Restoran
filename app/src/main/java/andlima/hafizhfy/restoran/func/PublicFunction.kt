package andlima.hafizhfy.restoran.func

import andlima.hafizhfy.restoran.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar

// Function to easy making Toast -------------------------------------------------------------------
fun toast(context: Context, message : String) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()
}

// Function to easy making SnackBar ----------------------------------------------------------------
fun snackbarLong(view: View, message: String) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snack.setAction("Ok") {
        snack.dismiss()
    }
    snack.show()
}

// Funtion to easy making Snackbar with custom action ----------------------------------------------
fun snackbarCustom(view: View, message: String, buttonText: String, action: Any.() -> Unit) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snack.setAction(buttonText) {
        action(true)
    }
    snack.show()
}

// Function to easy making SnackBar ----------------------------------------------------------------
fun snackbarIndefinite(view: View, message: String) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
    snack.setAction("Ok") {
        snack.dismiss()
    }
    snack.show()
}

// Function to easy making AlertDialog -------------------------------------------------------------
fun alertDialog(context: Context, title: String, message: String, action: Any.()->Unit) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            action(true)
        }
        .setCancelable(false)
        .show()
}

// Function to hide all error pop up ---------------------------------------------------------------
fun hideAllPopUp(cardView1: CardView, cardView2: CardView) {
    cardView1.visibility = View.GONE
    cardView2.visibility = View.GONE
}

// Function to show error pop up -------------------------------------------------------------------
fun showPopUp(cardViewID: CardView, textViewID: TextView, message: String) {
    cardViewID.visibility = View.VISIBLE
    textViewID.text = message
}

// Function to hide error pop up -------------------------------------------------------------------
fun hidePopUp(cardViewID: CardView) {
    cardViewID.visibility = View.GONE
}

// Function to show password on password EditText --------------------------------------------------
fun showPassword(editText: EditText, imageView: ImageView) {
    val hidden = PasswordTransformationMethod.getInstance()
    val show = HideReturnsTransformationMethod.getInstance()

    if (editText.transformationMethod == hidden) {
        editText.transformationMethod = show
        imageView.setImageResource(R.drawable.ic_eye_off)
    } else {
        editText.transformationMethod = hidden
        imageView.setImageResource(R.drawable.ic_eye)
    }
}