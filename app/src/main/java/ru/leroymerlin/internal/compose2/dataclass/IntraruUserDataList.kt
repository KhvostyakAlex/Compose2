package ru.leroymerlin.internal.compose2.dataclass


data class IntraruUserDataList(val account: String,
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
                               val workEmail: String,
                               var expandable: Boolean = false):BaseCellModel
