package `in`.iot.lab.dmscanner.ui

import `in`.iot.lab.dmscanner.databinding.ActivityLogInBinding
import `in`.iot.lab.dmscanner.model.UserInfo
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotpref.blockingBulk
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.Scopes
import com.google.firebase.auth.FirebaseAuth


class LogInActivity : AppCompatActivity() {

    val providers by lazy {
        arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder()
                .setScopes(listOf(Scopes.PROFILE,Scopes.EMAIL))
                .build()
        )
    }
    private lateinit var activityLogInBinding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (UserInfo.email!="NONE"){
            startMainActivity()
        }else{
            activityLogInBinding = ActivityLogInBinding.inflate(layoutInflater)
            setContentView(activityLogInBinding.root)
        }

    }

    override fun onResume() {
        super.onResume()
        activityLogInBinding.gsoBttn.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                val acct = GoogleSignIn.getLastSignedInAccount(this)
                UserInfo.blockingBulk {
                    name = user?.displayName ?:"NONE"
                    email = user?.email  ?:"NONE"
                    profile = (acct?.photoUrl?.toString()) ?:"NONE"
                }
                startMainActivity()
            } else {

            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}