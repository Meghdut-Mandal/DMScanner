package `in`.iot.lab.dmscanner.scanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity


fun <T : AppCompatActivity> AppCompatActivity.startActivityX(target: Class<T>) {
    val intent = Intent(this, target)
    startActivity(intent)
}