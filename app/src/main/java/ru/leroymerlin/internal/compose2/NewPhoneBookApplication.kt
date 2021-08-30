package ru.leroymerlin.internal.compose2

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class NewPhoneBookApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }

}