package ru.leroymerlin.internal.compose2.ui.screens.cards

import androidx.compose.runtime.Immutable

@Immutable
//data class ExpandableCardModel(val id: Int, val title: String)
data class ExpandableCardModel(val account: Int,
                               val firstName:String,
                               val lastName: String,
                               val orgUnitName:String,
                               val shopNumber:String,
                               val cluster: String,
                               val region: String,
                               val jobTitle: String,
                               val department: String,
                               val subDivision: String,
                               val workPhone: String,
                               val mobilePhone: String,
                               val personalEmail: String,
                               val workEmail: String)