package ru.leroymerlin.internal.phonebook.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.leroymerlin.internal.phonebook.repository.AuthApi
import ru.leroymerlin.internal.phonebook.repository.UserApi
import ru.leroymerlin.internal.phonebook.util.Constants.BASE_URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.*
import javax.security.cert.CertificateException

@Module
object AppModule {

/*
    @Singleton
    @Provides
    fun providePhonebookRepository(api: PhoneBookApi) = PhoneBookRepository(PhoneBookApi)*/

    @Singleton
    @Provides
    fun provideloggingInterceptor(): HttpLoggingInterceptor {
         val loggingInterceptor = HttpLoggingInterceptor()
         loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(provideloggingInterceptor())
            .build()
        return okHttpClient
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient())
            .client(getUnsafeOkHttpClientR()?.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //return  retrofit.create()
        return  retrofit
    }

    @Singleton
    @Provides
    fun  provideAuthApi(): AuthApi {
        return provideRetrofit().create()
    }

    @Singleton
    @Provides
    fun  provideUserApi(): UserApi {
        return provideRetrofit().create()
    }


   /* @Singleton
    @Provides
    fun  providePhonebookApi(): PhoneBookApi {
        return provideRetrofit().create()
    }

    @Singleton
    @Provides
    fun  providePhonebookApiOld(): PhoneBookApi {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

       val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .client(getUnsafeOkHttpClientR()?.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()


       // phoneBookApi = retrofit.create(PhoneBookApi::class.java)
      return  retrofit.create()
    }*/

    @Singleton
    @Provides
    fun getUnsafeOkHttpClientR(): OkHttpClient.Builder? {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.getSocketFactory()
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier(object : HostnameVerifier {
                override fun verify(hostname: String?, session: SSLSession?): Boolean {
                    return true
                }
            })
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

