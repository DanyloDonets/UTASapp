package donets.danylo.android.utasapp


import android.content.pm.PackageManager

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import androidx.core.content.ContextCompat


import donets.danylo.android.utasapp.databinding.ActivityMainBinding

import donets.danylo.android.utasapp.ui.screens.register.EnterPhoneNumberFragment
import donets.danylo.android.utasapp.ui.objects.appDrawer
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY
import donets.danylo.android.utasapp.database.AUTH

import donets.danylo.android.utasapp.utilits.READ_CONTACTS

import donets.danylo.android.utasapp.utilits.appStatus

import donets.danylo.android.utasapp.utilits.initContacts
import donets.danylo.android.utasapp.database.initFirebase
import donets.danylo.android.utasapp.database.initUser
import donets.danylo.android.utasapp.ui.screens.mainList.MainListFragment
import donets.danylo.android.utasapp.utilits.replaceFragment

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mToolbar:Toolbar
    lateinit var mAppDrawer: appDrawer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser{
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }
            initFields()
            initFunc()
        }
    }

    override fun onStart() {
        super.onStart()
        appStatus.updateStatus(appStatus.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        appStatus.updateStatus(appStatus.OFFLINE)
    }



    private fun initFunc() {
        setSupportActionBar(mToolbar)
        if (AUTH.currentUser!=null){


            mAppDrawer.create()
            replaceFragment(MainListFragment(), false)
        } else {
            replaceFragment(EnterPhoneNumberFragment(), false)
        }
    }




    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = appDrawer()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            initContacts()
        }
    }




}