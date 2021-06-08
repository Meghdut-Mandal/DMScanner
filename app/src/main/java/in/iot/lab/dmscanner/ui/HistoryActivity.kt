package `in`.iot.lab.dmscanner.ui

import `in`.iot.lab.dmscanner.R
import `in`.iot.lab.dmscanner.databinding.QrItemBinding
import `in`.iot.lab.dmscanner.model.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.text.SimpleDateFormat
import com.google.firebase.database.FirebaseDatabase
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import java.util.*

class HistoryActivity : AppCompatActivity() {

    val codesAdapter = GenericAdapter(QrItemBinding::inflate, ::itemHandler)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val rView=findViewById<RecyclerView>(R.id.qrList)
        rView.adapter=codesAdapter
        rView.layoutManager=LinearLayoutManager(this)
    }


    private fun itemHandler(qr: firebaseData, qrItemBinding: QrItemBinding) {
        if(qr.time!=0L){
            val date = Date(qr.time)
            qrItemBinding.dateField.text = SimpleDateFormat("EEE, MMM d").format(date)
        }
        qrItemBinding.nameDevice.text = qr.productId
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    override fun onResume(){
        super.onResume()
        val database=FirebaseDatabase.getInstance()
        val myRef=database.getReference("registrations")
        myRef.get().addOnSuccessListener{
            val dataList=arrayListOf<firebaseData>()
            for(datas in it.children){
                val obj= datas.getValue(firebaseData::class.java)!!
                if(obj.email==UserInfo.email){
                    dataList.add(obj)
                }
            }
            Log.d("Data",dataList.toString())
            codesAdapter.submitList(dataList)
        }
    }
}