package `in`.iot.lab.dmscanner.ui

import `in`.iot.lab.dmscanner.R
import `in`.iot.lab.dmscanner.databinding.QrItemBinding
import `in`.iot.lab.dmscanner.model.DeviceQr
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {

    val codesAdapter = GenericAdapter(QrItemBinding::inflate, ::itemHandler)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
    }


    private fun itemHandler(qr: DeviceQr, qrItemBinding: QrItemBinding) {
        val date = Date(qr.time)
        qrItemBinding.dateField.text = SimpleDateFormat("EEE, MMM d").format(date)
        qrItemBinding.nameDevice.text = qr.deviceName
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}