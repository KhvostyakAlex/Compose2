package ru.leroymerlin.internal.compose2.ui.screens


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.leroymerlin.internal.compose2.PhoneBookApi
import ru.leroymerlin.internal.compose2.dataclass.BaseCellModel
import ru.leroymerlin.internal.compose2.repository.PhoneBookRepository

import javax.inject.Inject


class LoginViewModel @Inject constructor(
    private val repository: PhoneBookRepository
): ViewModel(){

var authData = mutableStateOf<List<BaseCellModel>>(listOf())

    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)


    fun authIntraru(login:String, pass:String){
        viewModelScope.launch {
            val result = repository.authIntraru(login, pass)
        }
    }

}


