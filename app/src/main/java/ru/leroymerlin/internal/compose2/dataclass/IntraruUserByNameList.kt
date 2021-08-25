package ru.leroymerlin.internal.compose2.dataclass



data class IntraruUserByNameList (
    val hits: List<Hit>,
    val totalCount: Long
)

data class Hit (
    val id: String,
    val profile: Profile,
    val highlights: Highlights
)

data class Highlights (
    val lastName: List<String>,
    val fullName: List<String>
)

data class Profile (
    val account: String,
    val common: Common,
    val userStatus: UserStatus,
    val contacts: Contacts,
    val education: List<Any?>,
    val career: List<Career>,
    val skills: Any? = null,
    val personalInfo: PersonalInfo,
    val isAdmin: Boolean
)

data class CareerByName (
    val id: Long,
    val city: String,
    val organization: String,
    val department: String,
    val post: String,
    val startDate: String,
    val endDate: String? = null,
    val canEdit: Boolean
)

data class Common2 (
    val orgUnitType: String,
    val account: String,
    val firstName: String,
    val lastName: String,
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
    val orgUnitName: String,
    val shopNumber: String,
    val cluster: String,
    val region: String,
    val jobTitle: String,
    val department: String,
    val subDivisionCode: String,
    val subDivision: String,
    val controlledSubDivisions: List<Any?>,
    val smallAvatarURL: Any? = null,
    val mediumAvatarURL: Any? = null,
    val largeAvatarURL: Any? = null,
    val guideViewed: Boolean
)

data class Manager2 (
    val account: String,
    val fullName: String,
    val smallAvatarURL: Any? = null,
    val mediumAvatarURL: Any? = null,
    val largeAvatarURL: Any? = null
)

data class Contacts2 (
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

data class AlternativeEmailForNotifications2 (
    val value: String? = null,
    val isConfirmed: Boolean,
    val confrimCodeInfo: Any? = null
)

data class PersonalInfo2 (
    val hobby: Any? = null,
    val music: Any? = null,
    val films: Any? = null,
    val tvShow: Any? = null,
    val books: Any? = null,
    val games: Any? = null,
    val quotes: Any? = null,
    val aboutMe: Any? = null
)

data class UserStatus2 (
    val message: String,
    val systemMessage: String,
    val status: String,
    val showStatus: Boolean,
    val mentionedUsers: List<Any?>
)
