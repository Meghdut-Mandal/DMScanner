package `in`.iot.lab.dmscanner.model

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.bulk

object UserInfo : KotprefModel() {
    var email by stringPref("NONE")
    var name by stringPref("NONE")
    var profile by stringPref("NONE")
    fun logOut() {
        this.bulk {
            email = "NONE"
            name = "NONE"
            profile = "NONE"
        }
    }
}