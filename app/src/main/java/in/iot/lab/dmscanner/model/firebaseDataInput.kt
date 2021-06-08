package `in`.iot.lab.dmscanner.model

import android.util.Log
import com.firebase.ui.auth.data.model.User
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

        myRef.get().addOnSuccessListener{

            val firebaseDataobj=firebaseData(UserInfo.email,((it.childrenCount)+1).toInt()
            ,false,UserInfo.name,qr)
            myRef.child(firebaseDataobj.id.toString()).setValue(firebaseDataobj)


        }

    }


}