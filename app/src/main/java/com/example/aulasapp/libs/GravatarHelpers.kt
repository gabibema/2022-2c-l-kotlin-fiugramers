// requires StringHelpers

enum class GravatarDefault(val str: String) {
    ERROR_404 ("404"),
    MYSTERY ("mm"),
    IDENTICON ("identicon"),
    MONSTER ("monsterid"),
    WAVATAR ("wavatar"),
    RETRO ("retro"),
    BLANK ("blank"),
}

fun gravatarUrl(email: String,
                size: Int = 80,
                default: GravatarDefault = GravatarDefault.MYSTERY,
                secure: Boolean = false): String {
                
    val hash = email.trim().lowercase().md5()
    val protocol = if (secure) "https" else "http"
    return "$protocol://www.gravatar.com/avatar/$hash?s=$size&d=${default.str}"
}