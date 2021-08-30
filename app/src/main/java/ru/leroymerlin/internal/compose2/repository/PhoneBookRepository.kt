package ru.leroymerlin.internal.compose2.repository

import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import ru.leroymerlin.internal.compose2.PhoneBookApi
import ru.leroymerlin.internal.compose2.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserList
import ru.leroymerlin.internal.compose2.util.Resource
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class PhoneBookRepository @Inject constructor(
    private val api :PhoneBookApi
){
    suspend fun authIntraru(login:String, pass:String):
            Resource<Single<IntraruAuthUserList>> {
        val response = try {
            val body = JSONObject()
            body.put("login", "RU1000\\$login")
            body.put("password", pass)
            api.getUserInt( body.toString().toRequestBody("body".toMediaTypeOrNull()))
        } catch (e: Exception){
            return Resource.Error("Error authIntraru")
        }
        return Resource.Success(response)
    }

    suspend fun  getInfoUser(ldap: String, authHeader:String):
            Resource<Single<IntraruUserList?>> {
        val response = try {

            api.getUser(ldap, authHeader)

            //нужно сформировать DataList

        } catch (e: Exception){
            return Resource.Error("Error authIntraru")
        }
        return Resource.Success(response)
    }


}