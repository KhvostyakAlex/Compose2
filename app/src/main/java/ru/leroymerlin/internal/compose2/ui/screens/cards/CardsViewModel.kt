package ru.leroymerlin.internal.compose2.ui.screens.cards

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserByNameList
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.compose2.di.AppModule

class CardsViewModel : ViewModel() {

  //  private val _cards = MutableStateFlow(listOf<IntraruUserDataList>())
  //  val cards: StateFlow<List<IntraruUserDataList>> get() = _cards

    private val _cards: MutableLiveData<List<IntraruUserDataList>> = MutableLiveData()
    var cards: LiveData<List<IntraruUserDataList>> = _cards


  //  private val _expandedCardIdsList = MutableStateFlow(listOf<Int>())
   // val expandedCardIdsList: StateFlow<List<Int>> get() = _expandedCardIdsList

    private val _expandedCardIdsList = MutableLiveData(listOf<Int>())
    val expandedCardIdsList: LiveData<List<Int>> get() = _expandedCardIdsList



   // private val _cards = MutableLiveData<List<ExpandableCardModel>>(emptyList())
   // var cards: LiveData<List<ExpandableCardModel>> = _cards

   /* init {
        getFakeData()
    }*/

 /*   private  fun getFakeData() {
     /*   viewModelScope.launch(Dispatchers.Default) {
            val testList = arrayListOf<ExpandableCardModel>()
            repeat(3) { testList += ExpandableCardModel(id = it, title = "Карточка $it") }
            _cards.emit(testList)
        }*/
        viewModelScope.launch(Dispatchers.Default) {
            val testData = ArrayList<IntraruUserDataList>()

            testData.add(
                IntraruUserDataList(
                account = "60032246",
                firstName = "Алексей",
                lastName = "Хвостяк",
                orgUnitName = "Магазин Барнаул 1",
                shopNumber = "036",
                cluster =  "String",
                region =  "7",
                jobTitle =  "Специалист технической поддержки",
                department =  "department",
                subDivision =  "subDivision",
                workPhone =  "7999999998",
                mobilePhone =  "7999999999",
                personalEmail =  "personalEmail",
                workEmail = "Aleksey.Hvostyak@leroymerlin.ru",
            )
            )
            testData.add(IntraruUserDataList(
                account = "60032247",
                firstName = "Петр",
                lastName = "Иванов",
                orgUnitName = "Магазин Барнаул 2",
                shopNumber = "String",
                cluster =  "String",
                region =  "String",
                jobTitle =  "String",
                department =  "String",
                subDivision =  "String",
                workPhone =  "String",
                mobilePhone =  "String",
                personalEmail =  "String",
                workEmail = "String",
            ))
            testData.add(IntraruUserDataList(
                account = "60032248",
                firstName = "Иван",
                lastName = "Петров",
                orgUnitName = "Магазин Барнаул 2",
                shopNumber = "String",
                cluster =  "String",
                region =  "String",
                jobTitle =  "String",
                department =  "String",
                subDivision =  "String",
                workPhone =  "String",
                mobilePhone =  "String",
                personalEmail =  "String",
                workEmail = "String",
            ))
            _cards.postValue(testData)

        }




    }
    */


    private val _userData: MutableLiveData<List<IntraruUserDataList>> = MutableLiveData()
    var userData: LiveData<List<IntraruUserDataList>> = _userData

    private val _error = MutableLiveData<String>("")
    var error: LiveData<String> =_error

    fun getInfoUser(ldap: String, authHeader:String) {
        // Log.e("LOG mess", "response ldap "+ldap.toString())

        viewModelScope.launch(Dispatchers.Default) {
            AppModule.providePhonebookApi().getUser(ldap, authHeader)//здесь вызывается API
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val status = response?.userStatus?.status
                    val testData = ArrayList<IntraruUserDataList>()
Log.e("response", response.toString())
                    //добавляем в список всех работающих
                    if(status!="fired"){
                        testData.add(IntraruUserDataList(
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
                            response?.contacts!!.workEmails.toString(),
                        ))
                    }
                    //если ничего не нашлось
                    if(testData.isEmpty()){
                        testData
                    }
                   /* if(testData.isEmpty()){
                        testData.add(IntraruUserDataList(
                            "0",
                            "Ничего не найдено",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                        ))
                    }*/
                   // Log.e("testData", testData.toString())
                    _cards.postValue(testData)
                }, {
                    _error.postValue("Er - ${it.localizedMessage}")
                    val testData = ArrayList<IntraruUserDataList>()
                    _cards.postValue(testData)
                })
        }
    }


    fun getUserByName(userName: String, authHeader:String) {
        // Log.e("LOG mess", "userName - "+userName.toString())
        viewModelScope.launch(Dispatchers.Default) {
            AppModule.providePhonebookApi()
                .getUserByName(userName, authHeader)//здесь вызывается API
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val testData = ArrayList<IntraruUserDataList>()
                    val data = (response as IntraruUserByNameList).hits
                    // Log.e("LOG mess", "response mess "+(response as IntraruUserByNameList).toString())
                    // Log.e("LOG mess", "response mess 22 "+(response as IntraruUserByNameList).hits)


                    for (row in data) {

                        //  Log.e("LOG mess", "row - "+ row.profile.userStatus.status)
                        //   testData.add(IntraruArrLdapList(row.id))
                        val status = row.profile.userStatus.status
                        //добавляем в список всех работающих
                        if (status != "fired") {
                            val common = row.profile.common
                            val contacts = row.profile.contacts

                            testData.add(
                                IntraruUserDataList(
                                    common.account,
                                    common.firstName.toString(),
                                    common.lastName.toString(),
                                    common.orgUnitName.toString(),
                                    common.shopNumber.toString(),
                                    common.cluster.toString(),
                                    common.region.toString(),
                                    common.jobTitle.toString(),
                                    common.department.toString(),
                                    common.subDivision.toString(),
                                    contacts!!.workPhone?.value.toString(),
                                    contacts!!.mobilePhone?.value.toString(),
                                    contacts!!.personalEmail?.value.toString(),
                                    contacts!!.workEmails.toString(),
                                )
                            )
                        }
                    }
                   /* if (testData.isEmpty()) {
                        testData.add(
                            IntraruUserDataList(
                                "null",
                                "Ничего не найдено",
                                "",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",

                                )
                        )
                    }*/
                    _cards.postValue(testData)
                }, {
                    _error.postValue("Er - ${it.localizedMessage}")
                    //_error.postValue("Ничего не найдено")
                    val testData = ArrayList<IntraruUserDataList>()
                    _cards.postValue(testData)
                })


        }
    }






    fun onCardArrowClicked(cardId: Int) {
        _expandedCardIdsList.value = _expandedCardIdsList.value?.toMutableList().also { list ->
            if (list!!.contains(cardId)) list.remove(cardId) else list?.add(cardId)
        }
    }
}
