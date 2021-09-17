package ru.leroymerlin.internal.phonebook

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

//val currentTime = System.currentTimeMillis()/1000
fun Context.copyToClipboard(text: CharSequence){
    val myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
    val myClip: ClipData? = ClipData.newPlainText("text", text)

    myClipboard?.setPrimaryClip(myClip!!)
}

@SuppressLint("ServiceCast")
fun hideKeyboard(activity: Activity) {
    val imm: InputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.snackbar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

fun View.snackbar2(message: String) {
    val snack: Snackbar =  Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
    val view = snack.view
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.CENTER_VERTICAL
    view.layoutParams = params
    snack.setAction("Ok") {
        snack.dismiss()
    }
    snack.show()
}



fun getCalculatedDate(dateFormat: String?, days: Int): String? { //функция высчитывает дату от сегодня
    val cal = Calendar.getInstance()
    val s = SimpleDateFormat(dateFormat)
    cal.add(Calendar.DAY_OF_YEAR, days)
    return s.format(Date(cal.timeInMillis))
}

fun addToFB(node:String, account:String, value:String? ="ok"){
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference(node)
        .child("account")
        .child(account)
        .child(getCalculatedDate("yyyy-MM-dd", 0).toString())
        .child(getCalculatedDate("HH:mm:ss", 0).toString())
    myRef.setValue(value)
}





