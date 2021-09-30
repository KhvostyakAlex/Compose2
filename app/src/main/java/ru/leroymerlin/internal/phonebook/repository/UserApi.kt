package ru.leroymerlin.internal.phonebook.repository

import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*
import ru.leroymerlin.internal.phonebook.dataclass.*



interface UserApi {

    @GET(" https://intraru3.leroymerlin.ru/services/profiles/api/profiles/{ldap}")
    fun getUser(@Path("ldap") ldap: String,
                @Header("Authorization") authHeader: String):Single<IntraruUserList?>

    @GET("https://intraru3.leroymerlin.ru/services/search/api/integrations/callcenter/profiles?searchString=")
    fun getUserNew(@Query("ldap") ldap: String,
                @Header("Authorization") authHeader: String):Single<IntraruUserList?>


    @GET(" https://intraru3.leroymerlin.ru/services/search/api/Search/profiles?count=50&filters=")
    fun getUserByName(@Query("searchString") userName: String,
                      @Header("Authorization") authHeader: String):Single<IntraruUserByNameList?>

    @GET("https://intraru3.leroymerlin.ru/services/search/api/Search/profiles?count=200&")
    fun getUserByDepartment(@Query("filters") filters: String,
                            @Header("Authorization") authHeader: String):Single<IntraruUserByNameList?>

    @GET("https://intraru3.leroymerlin.ru/services/search/api/Search/orgunitcards?count=200&filters=&searchString=&tab=orgunitcards")
    fun getDepartment(@Header("Authorization") authHeader: String):Single<DepartmentList?>

}

