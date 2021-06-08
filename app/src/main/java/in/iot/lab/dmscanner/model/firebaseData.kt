package `in`.iot.lab.dmscanner.model

data class firebaseData(val email:String,val id:Int,val isApproved:Boolean,val name:String,val productId:String,
                        val time:Long=System.currentTimeMillis()) {
    constructor():this("",0,false,"","",0)
}