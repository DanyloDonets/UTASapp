package donets.danylo.android.utasapp.ui.screens.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import donets.danylo.android.utasapp.databinding.FragmentEnterCodeBinding
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY
import donets.danylo.android.utasapp.database.AUTH
import donets.danylo.android.utasapp.utilits.AppTextWatcher
import donets.danylo.android.utasapp.database.CHILD_ID
import donets.danylo.android.utasapp.database.CHILD_PHONE
import donets.danylo.android.utasapp.database.CHILD_USERNAME
import donets.danylo.android.utasapp.database.NODE_PHONES
import donets.danylo.android.utasapp.database.NODE_USERS
import donets.danylo.android.utasapp.database.REF_DATABASE_ROOT
import donets.danylo.android.utasapp.utilits.AppValueEventListener
import donets.danylo.android.utasapp.utilits.restartActivity
import donets.danylo.android.utasapp.utilits.showToast

class EnterCodeFragment(val mPhoneNumber: String, val id: String) : Fragment() {



    private var _binding: FragmentEnterCodeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        APP_ACTIVITY.title = mPhoneNumber
        binding.registerInputCode.addTextChangedListener(AppTextWatcher {

            val string = binding.registerInputCode.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code:String = binding.registerInputCode.text.toString()
        Log.i("Loging", "code ="+code)
        val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(id,code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if(task.isSuccessful){
                showToast("Welcome")
                val uid:String = AUTH.currentUser?.uid.toString()
                val dataMap = mutableMapOf<String,Any>()
                dataMap[CHILD_ID] = uid
                dataMap[CHILD_PHONE] = mPhoneNumber

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                    .addListenerForSingleValueEvent(AppValueEventListener{

                        if (!it.hasChild(CHILD_USERNAME)){
                            dataMap[CHILD_USERNAME] = uid
                        }

                        REF_DATABASE_ROOT.child(
                            NODE_PHONES
                        ).child(mPhoneNumber).setValue(uid)
                            .addOnFailureListener { showToast(it.message.toString()) }
                            .addOnSuccessListener {
                                REF_DATABASE_ROOT.child(
                                    NODE_USERS
                                ).child(uid).updateChildren(dataMap)
                                    .addOnSuccessListener {
                                        showToast("Добро пожаловать")
                                        restartActivity()
                                    }
                                    .addOnFailureListener { showToast(it.message.toString()) }
                            }
                    })



            }else{
                showToast(task.exception?.message.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leak
    }
}
