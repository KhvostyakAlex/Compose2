package cru.leroymerlin.internal.compose2.ui.screens.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserDataList

class CardsViewModel : ViewModel() {

    private val _cards = MutableStateFlow(listOf<IntraruUserDataList>())
    val cards: StateFlow<List<IntraruUserDataList>> get() = _cards

   // private val _cards = MutableStateFlow(listOf<ExpandableCardModel>())
  //  val cards: StateFlow<List<ExpandableCardModel>> get() = _cards


    private val _expandedCardIdsList = MutableStateFlow(listOf<Int>())
    val expandedCardIdsList: StateFlow<List<Int>> get() = _expandedCardIdsList

   // private val _cards = MutableLiveData<List<ExpandableCardModel>>(emptyList())
   // var cards: LiveData<List<ExpandableCardModel>> = _cards

    init {
        getFakeData()
    }

    private  fun getFakeData() {
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
            _cards.emit(testData)
        }




    }

    fun onCardArrowClicked(cardId: Int) {
        _expandedCardIdsList.value = _expandedCardIdsList.value.toMutableList().also { list ->
            if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
        }
    }
}
