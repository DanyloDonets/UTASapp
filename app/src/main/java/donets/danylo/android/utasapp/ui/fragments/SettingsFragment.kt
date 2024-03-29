package donets.danylo.android.utasapp.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.UCrop
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.activities.RegisterActivity
import donets.danylo.android.utasapp.databinding.FragmentSettingsBinding
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY
import donets.danylo.android.utasapp.utilits.AUTH
import donets.danylo.android.utasapp.utilits.CHILD_PHOTO_URL
import donets.danylo.android.utasapp.utilits.CURRENT_UID
import donets.danylo.android.utasapp.utilits.FOLDER_PROFILE_IMAGE
import donets.danylo.android.utasapp.utilits.NODE_USERS
import donets.danylo.android.utasapp.utilits.REF_DATABASE_ROOT
import donets.danylo.android.utasapp.utilits.REF_STORAGE_ROOT
import donets.danylo.android.utasapp.utilits.USER
import donets.danylo.android.utasapp.utilits.downloadAndSetImage
import donets.danylo.android.utasapp.utilits.getUrlFromStorage
import donets.danylo.android.utasapp.utilits.putImageToStorage
import donets.danylo.android.utasapp.utilits.putUrlToDatabase
import donets.danylo.android.utasapp.utilits.replaceActivity
import donets.danylo.android.utasapp.utilits.replaceFragment
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
            .maxResultSize(600, 600)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {

            val resultUri: Uri? = data?.data!!
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)
            if (resultUri != null) {
                putImageToStorage(resultUri, path){
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
                AUTH.signOut()
                APP_ACTIVITY.replaceActivity(RegisterActivity())
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
