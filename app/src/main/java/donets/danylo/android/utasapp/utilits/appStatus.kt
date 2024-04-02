package donets.danylo.android.utasapp.utilits

import donets.danylo.android.utasapp.database.AUTH
import donets.danylo.android.utasapp.database.CHILD_STATUS

import donets.danylo.android.utasapp.database.CURRENT_UID
import donets.danylo.android.utasapp.database.NODE_USERS
import donets.danylo.android.utasapp.database.REF_DATABASE_ROOT
import donets.danylo.android.utasapp.database.USER

enum class appStatus(val status:String) {
    ONLINE("В мережі"),
    OFFLINE("Не в мережі"),
    TYPING("Друкує...");

    companion object{
        fun updateStatus(appStatus: appStatus){
            if (AUTH.currentUser!=null){
                REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATUS)
                    .setValue(appStatus.status)
                    .addOnSuccessListener { USER.status = appStatus.status }
                    .addOnFailureListener { showToast(it.message.toString()) }
            }
        }
    }
}