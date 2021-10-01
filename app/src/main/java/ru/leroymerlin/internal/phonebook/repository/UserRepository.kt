package ru.leroymerlin.internal.phonebook.repository

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.leroymerlin.internal.phonebook.dataclass.DepartmentList
import ru.leroymerlin.internal.phonebook.dataclass.IntraruUserByNameList
import ru.leroymerlin.internal.phonebook.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.phonebook.dataclass.IntraruUserList
import ru.leroymerlin.internal.phonebook.di.AppModule
import ru.leroymerlin.internal.phonebook.di.AppModule.provideUserApi

class UserRepository:UserApi {
    override fun getUser(ldap: String, authHeader: String): Single<IntraruUserList?> {
        val testData = ArrayList<IntraruUserDataList>()
   /*    provideUserApi().getUser(ldap, authHeader)//здесь вызывается API
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({response ->

                testData.add(
                    IntraruUserDataList(
                    response?.common!!.account,
                    response?.common!!.firstName.toString(),
                    response?.common!!.lastName.toString(),
                    response?.common!!.orgUnitName.toString(),
                    response?.common!!.shopNumber.toString(),
                    response?.common!!.cluster.toString(),
                    response?.common!!.region.toString(),
                    response?.common!!.jobTitle.toString(),
                    response?.common!!.department.toString(),
                    response?.common!!.subDivision.toString(),
                    response?.contacts!!.workPhone?.value.toString(),
                    response?.contacts!!.mobilePhone?.value.toString(),
                    response?.contacts!!.personalEmail?.value.toString(),
                    response?.contacts!!.workEmails?.toString(),

                    )
                )
                return
               // _userData.postValue(testData)

            }, {
               // _error.postValue("Ошибка - ${it.localizedMessage}")
            })
        return Single*/
        //return Single<IntraruUserList>
        TODO("Not yet implemented")
    }


    override fun getUserNew(ldap: String, authHeader: String): Single<IntraruUserList?> {
        TODO("Not yet implemented")
    }

    override fun getUserByName(
        userName: String,
        authHeader: String
    ): Single<IntraruUserByNameList?> {
        TODO("Not yet implemented")
    }

    override fun getUserByDepartment(
        filters: String,
        authHeader: String
    ): Single<IntraruUserByNameList?> {
        TODO("Not yet implemented")
    }

    override fun getDepartment(authHeader: String): Single<DepartmentList?> {
        TODO("Not yet implemented")
    }
}