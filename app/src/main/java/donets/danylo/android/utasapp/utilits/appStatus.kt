package donets.danylo.android.utasapp.utilits

enum class appStatus(val status:String) {
    ONLINE("В мережі"),
    OFFLINE("Не в мережі"),
    TYPING("Друкує...");

    companion object{
        fun updateStatus(appStatus: appStatus){
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATUS)
                .setValue(appStatus.status)
                .addOnSuccessListener { USER.status = appStatus.status }
                .addOnFailureListener {
                    showToast(it.message.toString())
                }
        }
    }
}