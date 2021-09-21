package ru.leroymerlin.internal.phonebook.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class SettingsModel(
    val title: String,
    val value: String
)

class SettingsViewModel: AndroidViewModel (Application()){

    val activity = getApplication<Application>()


private val _settings: MutableLiveData<List<SettingsModel>> = MutableLiveData(
    listOf(
        SettingsModel("Имя", "Алексей"),
        SettingsModel("№ магазина", "036"),
        SettingsModel("Должность", "СТП"),
        SettingsModel("Телефон", "+79099999999")
       // SettingsModel("expiresIn", "+79099999999")
       // SettingsModel("expiresOn", "+79099999999")
    )
)
val settings: LiveData<List<SettingsModel>> = _settings

    fun getSettings(){
/*
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val a = activity.getSharedPreferences()

        val token = sharedPref.getString("token", "") //достаем данные из shared prefs

 */
    }



}