package ru.leroymerlin.internal.compose2

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
//@InstallIn()
object AppModule {
    @Singleton
    @Provides
    fun providePhonebookRepository(api:PhoneBookApi) = PhonebookRepository(api)
}

