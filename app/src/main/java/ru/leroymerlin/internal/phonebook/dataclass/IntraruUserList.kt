package ru.leroymerlin.internal.phonebook.dataclass


data class IntraruUserList (
    val account: String,
    val common: Common,
    val userStatus: UserStatus,
    val contacts: Contacts,
    val education: List<Any?>,
    val career: List<Career>,
    val skills: List<Any?>,
    val personalInfo: PersonalInfo,
    val isAdmin: Boolean
)

data class Career (
    val id: Long,
    val city: String,
    val organization: String,
    val department: String,
    val post: String,
    val startDate: String,
    val endDate: String? = null,
    val canEdit: Boolean
)

data class Common (
    val account: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val gender: String,
    val city: String,
    val domeLevel: String,
    val birthDay: String,
    val birthDayDisplayFormat: String,
    val manager: Manager,
    val responsibilityArea: Any? = null,
    val commitees: List<Any?>,
    val hireDate: String,
    val orgUnitCardID: String,
    val orgUnitName: String? =null,
    val shopNumber: String? = null,
    val cluster: String? = null,
    val region: String? = null,
    val jobTitle: String? = null,
    val department: String? = null,
    val subDivisionCode: String,
    val subDivision: String? = null,
    val controlledSubDivisions: List<Any?>,
    val smallAvatarURL: Any? = null,
    val mediumAvatarURL: Any? = null,
    val largeAvatarURL: Any? = null,
    val guideViewed: Boolean,
    val orgUnitType: String,
    val shopFormat: String
)

data class Manager (
    val account: String,
    val fullName: String,
    val smallAvatarURL: Any? = null,
    val mediumAvatarURL: Any? = null,
    val largeAvatarURL: Any? = null
)

data class Contacts (
    val workPhone: AlternativeEmailForNotifications,
    val mobilePhone: AlternativeEmailForNotifications,
    val digitalSignaturePhone: AlternativeEmailForNotifications,
    val workEmails: List<Any?>,
    val personalEmail: AlternativeEmailForNotifications,
    val alternativeEmailForNotifications: AlternativeEmailForNotifications,
    val vkURL: Any? = null,
    val okURL: Any? = null,
    val facebookURL: Any? = null,
    val instagram: Any? = null,
    val skype: Any? = null,
    val telegram: Any? = null,
    val employeeAdditionalNumber: Any? = null,
    val myWorkplace: Any? = null
)

data class AlternativeEmailForNotifications (
    val value: String? = null,
    val isConfirmed: Boolean,
    val confrimCodeInfo: Any? = null
)

data class PersonalInfo (
    val hobby: Any? = null,
    val music: Any? = null,
    val films: Any? = null,
    val tvShow: Any? = null,
    val books: Any? = null,
    val games: Any? = null,
    val quotes: Any? = null,
    val aboutMe: Any? = null
)

data class UserStatus (
    val message: String,
    val systemMessage: String,
    val status: String,
    val showStatus: Boolean,
    val mentionedUsers: List<Any?>
)
