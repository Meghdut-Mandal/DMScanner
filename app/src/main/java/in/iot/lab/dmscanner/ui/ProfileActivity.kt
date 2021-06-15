package `in`.iot.lab.dmscanner.ui

import `in`.iot.lab.dmscanner.R
import `in`.iot.lab.dmscanner.databinding.ActivityProfileBinding
import `in`.iot.lab.dmscanner.model.UserInfo
import `in`.iot.lab.dmscanner.scanner.startActivityX
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.NonNull
import coil.load
import com.firebase.ui.auth.data.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(this, gso)

        binding.logoutButton.setOnClickListener {
            client.signOut().addOnCompleteListener {
                FirebaseAuth.getInstance().signOut()
                UserInfo.logOut()
                startActivityX(LogInActivity::class.java)
                finishAffinity()
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}