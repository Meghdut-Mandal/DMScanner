package `in`.iot.lab.dmscanner.ui

import `in`.iot.lab.dmscanner.R
import `in`.iot.lab.dmscanner.databinding.ActivityProfileBinding
import `in`.iot.lab.dmscanner.model.UserInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.NonNull
import coil.load
import com.firebase.ui.auth.data.model.User

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        if (UserInfo.profile!="NONE"){
            binding.profileImage.load(UserInfo.profile)
        }
        binding.userEmail.text=UserInfo.email
        binding.userName.text=UserInfo.name
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}