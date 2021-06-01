package `in`.iot.lab.dmscanner.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class firebaseDataInput {

    fun putInDataBase(qr:String){
        val database=FirebaseDatabase.getInstance()
        val myRef=database.getReference("registrations")
        Log.d("id",UserInfo.email)



        firebaseData.email=UserInfo.email
        firebaseData.name=UserInfo.name
        firebaseData.isApproved=false
        firebaseData.productId=qr

        myRef.get().addOnSuccessListener{
            firebaseData.id=((it.childrenCount)+1).toString()
            Log.d("id",firebaseData.id)
            myRef.child(firebaseData.id).setValue(firebaseData)
        }

//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//            }
//            override fun onCancelled(error: DatabaseError) {}
//        })



    }


}