package `in`.iot.lab.dmscanner.ui

import `in`.iot.lab.dmscanner.databinding.ActivityLogInBinding
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log


class LogInActivity : AppCompatActivity() {

    val providers by lazy {
        arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build())
    }
    private lateinit var activityLogInBinding:ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLogInBinding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(activityLogInBinding.root)
    }

    override fun onResume() {
        super.onResume()
        activityLogInBinding.gsoBttn.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                println(">>> ${user.email}")
            } else {

            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}