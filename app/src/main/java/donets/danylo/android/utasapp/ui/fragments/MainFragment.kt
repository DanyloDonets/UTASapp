package donets.danylo.android.utasapp.ui.fragments

import androidx.fragment.app.Fragment
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY
import donets.danylo.android.utasapp.utilits.hideKeyboard


class MainFragment : Fragment(R.layout.fragment_chats) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title= "Чати"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
    }
}