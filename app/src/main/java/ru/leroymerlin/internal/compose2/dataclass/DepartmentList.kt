package ru.leroymerlin.internal.compose2.dataclass

data class DepartmentList (
    val hits: List<HitDepartment>,
    val totalCount: Long
)

data class HitDepartment (
    val id: String,
    val orgUnitCard: OrgUnitCard,
    val highlights: HighlightsDepartment
)

class HighlightsDepartment()

data class OrgUnitCard (
    val id: String,
    val name: String,
    val orgUnitType: String,
    val firmID: String,
    val orgUnitNumber: String,
    val smallAvatarURL: Any? = null,
    val mediumAvatarURL: Any? = null,
    val largeAvatarURL: Any? = null,
    val region: Any? = null,
    val regionID: Any? = null,
    val shopFormat: String,
    val cluster: String,
    val clusterID: String,
    val country: String,
    val countryID: String,
    val city: String,
    val cityID: String,
    val status: String
)


