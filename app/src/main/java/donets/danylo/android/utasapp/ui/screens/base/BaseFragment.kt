package donets.danylo.android.utasapp.ui.screens.base

import androidx.fragment.app.Fragment
import donets.danylo.android.utasapp.ui.messageRecyclerView.views.MessageView
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY


open class BaseFragment(layout: Int) : Fragment(layout) {



    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }



}