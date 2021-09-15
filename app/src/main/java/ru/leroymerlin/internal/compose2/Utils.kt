package ru.leroymerlin.internal.compose2

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.annotation.AnyRes
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

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





fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}





const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

fun checkPermissionREAD_EXTERNAL_STORAGE(
    context: Context?
): Boolean {
    val currentAPIVersion = Build.VERSION.SDK_INT
    return if (currentAPIVersion >= Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (context as Activity?)!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                showDialog(
                    "External storage", context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } else {
                ActivityCompat
                    .requestPermissions(
                        (context as Activity?)!!,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
            }
            false
        } else {
            true
        }
    } else {
        true
    }
}

fun checkPermissionWRITE_EXTERNAL_STORAGE(
    context: Context?
): Boolean {
    val currentAPIVersion = Build.VERSION.SDK_INT
    return if (currentAPIVersion >= Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (context as Activity?)!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                showDialog(
                    "External storage", context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } else {
                ActivityCompat
                    .requestPermissions(
                        (context as Activity?)!!,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
            }
            false
        } else {
            true
        }
    } else {
        true
    }
}

fun showDialog(
    msg: String, context: Context?,
    permission: String
) {
    val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(context!!)
    alertBuilder.setCancelable(true)
    alertBuilder.setTitle("Permission necessary")
    alertBuilder.setMessage("$msg permission is necessary")
    alertBuilder.setPositiveButton(
        R.string.yes,
        { dialog, which ->
            ActivityCompat.requestPermissions(
                (context as Activity?)!!, arrayOf(permission),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        })
    val alert: AlertDialog = alertBuilder.create()
    alert.show()
}

fun getUriToDrawable(
    context: Context,
    @AnyRes drawableId: Int
): Uri {
    return Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId)
    )
}





suspend fun <T> withIO(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.IO, block)

fun getCalculatedDate(dateFormat: String?, days: Int): String? { //функция высчитывает дату от сегодня
    val cal = Calendar.getInstance()
    val s = SimpleDateFormat(dateFormat)
    cal.add(Calendar.DAY_OF_YEAR, days)
    return s.format(Date(cal.timeInMillis))
}

fun getDate(): String? {
    // val sdf = SimpleDateFormat("dd_M_yyyy")
    val sdt = SimpleDateFormat("hh")
    sdt.timeZone = TimeZone.getTimeZone("Europe/Moscow")
    val currentTime = sdt.format(Date())//находим текущий час

    if(currentTime.toInt()<5){ //если меньше 5 часов, то вчерашняя тдата
        return  getCalculatedDate("dd_M_yyyy", -1)
    }else{
        return getCalculatedDate("dd_M_yyyy", 0)
    }
}

fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String):String{
    if (body==null)
        return ""
    var input: InputStream? = null
    try {
        input = body.byteStream()
        //val file = File(getCacheDir(), "cacheFileAppeal.srl")
        val fos = FileOutputStream(pathWhereYouWantToSaveFile)
        fos.use { output ->
            val buffer = ByteArray(4 * 1024) // or other buffer size
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                output.write(buffer, 0, read)
            }
            output.flush()
        }
        return pathWhereYouWantToSaveFile
    }catch (e: Exception){

    }
    finally {
        input?.close()
    }
    return ""
}





