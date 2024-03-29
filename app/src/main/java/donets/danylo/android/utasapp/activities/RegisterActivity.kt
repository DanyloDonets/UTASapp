package donets.danylo.android.utasapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.databinding.ActivityRegisterBinding
import donets.danylo.android.utasapp.ui.fragments.EnterPhoneNumberFragment
import donets.danylo.android.utasapp.utilits.initFirebase
import donets.danylo.android.utasapp.utilits.replaceFragment


class RegisterActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFirebase()
    }

    override fun onStart() {
        super.onStart()
        mToolbar = mBinding.registerToolbar
        setSupportActionBar(mToolbar)
        title = getString(R.string.register_title_your_phone)
        replaceFragment(EnterPhoneNumberFragment(), false)
    }
}