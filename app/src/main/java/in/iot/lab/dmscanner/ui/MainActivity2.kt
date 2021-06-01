package `in`.iot.lab.dmscanner.ui

import `in`.iot.lab.dmscanner.R
import `in`.iot.lab.dmscanner.databinding.ActivityMainBinding
import `in`.iot.lab.dmscanner.databinding.QrScannedBinding
import `in`.iot.lab.dmscanner.scanner.CodeScanner
import `in`.iot.lab.dmscanner.scanner.ErrorCallback
import `in`.iot.lab.dmscanner.scanner.Utils
import `in`.iot.lab.dmscanner.model.firebaseDataInput
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.zxing.Result
import org.opencv.android.OpenCVLoader
import java.io.FileNotFoundException

class MainActivity2 : AppCompatActivity() {
    private var fireBaseInput=firebaseDataInput()
    private var mainBinding: ActivityMainBinding? = null
    private lateinit var mCodeScanner: CodeScanner
    private var mPermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {

        OpenCVLoader.initDebug()
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding!!.root)
        mCodeScanner = CodeScanner(this, mainBinding!!.scannerView)
        mCodeScanner.setDecodeCallback { result: Result ->
            fireBaseInput.putInDataBase(result.text.replace("1 tags found","").trim())
            runOnUiThread {
                displayQR(result.text)
            }

        }

        mCodeScanner.setErrorCallback(ErrorCallback { error: Exception? ->
            runOnUiThread {
                Toast.makeText(
                    this,
                    "Error in scanning!!",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false
                requestPermissions(arrayOf(Manifest.permission.CAMERA), RC_PERMISSION)
            } else {
                mPermissionGranted = true
            }
        } else {
            mPermissionGranted = true
        }
    }

    override fun onResume() {
        super.onResume()
        mainBinding!!.textView.setOnClickListener { view: View? -> showChooser() }
        if (mPermissionGranted) {
            mCodeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true
                mCodeScanner.startPreview()
            } else {
                mPermissionGranted = false
            }
        }
    }

    override fun onPause() {
        mCodeScanner.releaseResources()
        super.onPause()
    }

    fun showChooser() {
        val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        photoPickerIntent.type = "*/*"
        photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(photoPickerIntent, "Select a File to Upload"),
            REQ_CODE_PICK_IMAGE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQ_CODE_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                val selectedImage = intent!!.data
                try {
                    val inputStream = contentResolver.openInputStream(
                        selectedImage!!
                    )
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    extracted(bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == REQ_CODE_CAMERA) {
            val img = intent!!.extras!!["data"] as Bitmap?
            extracted(img)
        }
    }

    private fun extracted(img: Bitmap?) {
        val text = Utils.dmtxDecode2(img)
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = text
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        private const val REQ_CODE_PICK_IMAGE = 0
        private const val REQ_CODE_CAMERA = 1
        private const val RC_PERMISSION = 10
    }

    fun displayQR(qr: String) {
        val name = qr.toByteArray().sum().toString(26)
        MaterialDialog(this, BottomSheet()).show {
            customView(R.layout.qr_scanned)
            val scannedBinding = QrScannedBinding.bind(this.getCustomView())
            scannedBinding.watchName.text="Titan $name"
        }
    }


}