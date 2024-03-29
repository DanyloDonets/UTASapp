package donets.danylo.android.utasapp.ui.fragments

import android.os.Bundle
import android.text.BoringLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import donets.danylo.android.utasapp.MainActivity
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY


open class BaseFragment(layout: Int) : Fragment(layout) {



    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

    override  fun onStop(){
        super.onStop()
        APP_ACTIVITY.mAppDrawer.enableDrawer()
    }
}