package `in`.iot.lab.dmscanner.ui

import `in`.iot.lab.dmscanner.R
import `in`.iot.lab.dmscanner.databinding.ActivityHistoryBinding
import `in`.iot.lab.dmscanner.databinding.ActivityHomeBinding
import `in`.iot.lab.dmscanner.scanner.startActivityX
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.NonNull

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.historyButton.setOnClickListener {
            startActivityX(HistoryActivity::class.java)
        }
        binding.profileButton.setOnClickListener {
            startActivityX(ProfileActivity::class.java)
        }
        binding.scanButton.setOnClickListener {
            startActivityX(MainActivity2::class.java)
        }
    }
}