package `in`.iot.lab.dmscanner.model

import com.chibatching.kotpref.KotprefModel

object UserInfo : KotprefModel() {
    var email by stringPref("NONE")
    var name by stringPref("NONE")

}