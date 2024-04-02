package donets.danylo.android.utasapp.ui.screens.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.databinding.FragmentSettingsBinding
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY
import donets.danylo.android.utasapp.database.AUTH
import donets.danylo.android.utasapp.database.CURRENT_UID
import donets.danylo.android.utasapp.database.FOLDER_PROFILE_IMAGE
import donets.danylo.android.utasapp.database.REF_STORAGE_ROOT
import donets.danylo.android.utasapp.database.USER
import donets.danylo.android.utasapp.utilits.appStatus
import donets.danylo.android.utasapp.utilits.downloadAndSetImage
import donets.danylo.android.utasapp.database.getUrlFromStorage
import donets.danylo.android.utasapp.database.putFileToStorage

import donets.danylo.android.utasapp.database.putUrlToDatabase
import donets.danylo.android.utasapp.utilits.replaceFragment
import donets.danylo.android.utasapp.utilits.restartActivity
import donets.danylo.android.utasapp.utilits.showToast


class SettingsFragment : Fragment() {


    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        with(binding) {
            settingsBio.text = USER.bio
            settingsFullName.text = USER.fullname
            settingsPhoneNumber.text = USER.phone
            settingsStatus.text = USER.status
            settingsUsername.text = USER.username
            settingsBtnChangeUsername.setOnClickListener { replaceFragment(ChangeUsernameFragment())}
            settingsBtnChangeBio.setOnClickListener { replaceFragment(ChangeBioFragment()) }
            settingsChangePhoto.setOnClickListener { changePhotoUser() }
            binding.settingsUserPhoto.downloadAndSetImage(USER.photoUrl)
        }
    }

    private fun changePhotoUser() {
        ImagePicker.with(this)
            .crop() //Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(250, 250)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {

            val resultUri: Uri? = data?.data!!
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)
            if (resultUri != null) {
                putFileToStorage(resultUri, path){
                    getUrlFromStorage(path){
                        putUrlToDatabase(it){
                            binding.settingsUserPhoto.downloadAndSetImage(it)
                            showToast("Updated")
                            USER.photoUrl = it
                            APP_ACTIVITY.mAppDrawer.updateHeader()
                        }
                    }
                }
            }
        }else if (resultCode == ImagePicker.RESULT_ERROR) {

        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_menu_exit -> {
                appStatus.updateStatus(appStatus.OFFLINE)
                AUTH.signOut()
                restartActivity()
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
