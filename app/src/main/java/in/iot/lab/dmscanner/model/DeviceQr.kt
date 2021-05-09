package `in`.iot.lab.dmscanner.model


data class DeviceQr(val time:Long, val deviceName:String, val qrValue:String):Identifiable {
    override val id: String
        get() = time.toString()
}