package ru.leroymerlin.internal.compose2.dataclass

data class Task(val idTask: Int, val codeName:String, /*val apiLevel:String,*/ val description: String, var expandable: Boolean = false) :
    BaseCellModel