@file:Suppress("DEPRECATION")

package donets.danylo.android.utasapp.ui.fragments.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import donets.danylo.android.utasapp.MainActivity
import donets.danylo.android.utasapp.R

import donets.danylo.android.utasapp.databinding.FragmentEnterPhoneNumberBinding
import donets.danylo.android.utasapp.database.AUTH
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY

import donets.danylo.android.utasapp.utilits.replaceFragment
import donets.danylo.android.utasapp.utilits.restartActivity
import donets.danylo.android.utasapp.utilits.showToast
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment : Fragment() {

    private lateinit var  mPhoneNumber:String
    private lateinit var mCallBack:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var _binding: FragmentEnterPhoneNumberBinding? = null

    // Цей властивість дозволяє безпечно використовувати _binding в усьому коді фрагмента.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        showToast("Welcome")
                        restartActivity()
                    }else{
                        showToast(task.exception?.message.toString())
                    }
                }

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber,id))
            }
        }
        // Використовуємо binding для встановлення обробника натискань.
        binding.registerBtnNext.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (binding.registerInputPhoneNumber.text.toString().isEmpty()) {
            showToast(getString(R.string.register_toast_enter_phone))
        } else {

            authUser()

        }
    }

    private fun authUser() {
        mPhoneNumber = binding.registerInputPhoneNumber.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mPhoneNumber,
            60,
            TimeUnit.SECONDS,
            APP_ACTIVITY,
            mCallBack
        )  }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Очищення посилання на біндінг для уникнення витоку пам'яті
    }
}
