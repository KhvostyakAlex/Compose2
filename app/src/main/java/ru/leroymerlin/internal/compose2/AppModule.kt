package ru.leroymerlin.internal.compose2

@Module
//@InstallIn()
object AppModule {
    @Singleton
    @Provides
    fun providePhonebookRepository(api:PhoneBookApi) = PhonebookRepository(api)
}