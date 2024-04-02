package donets.danylo.android.utasapp.ui.screens.groups

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.database.createGroupToDatabase
import donets.danylo.android.utasapp.databinding.FragmentCreateGroupBinding
import donets.danylo.android.utasapp.models.CommonModel
import donets.danylo.android.utasapp.ui.screens.base.BaseFragment
import donets.danylo.android.utasapp.ui.screens.mainList.MainListFragment
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY
import donets.danylo.android.utasapp.utilits.getPlurals
import donets.danylo.android.utasapp.utilits.hideKeyboard
import donets.danylo.android.utasapp.utilits.replaceFragment
import donets.danylo.android.utasapp.utilits.showToast

class CreateGroupFragment(private var listContacts:List<CommonModel>): BaseFragment(R.layout.fragment_create_group) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private var _binding: FragmentCreateGroupBinding? = null
    private val binding get() = _binding!!
    private var mUri = Uri.EMPTY


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreateGroupBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        hideKeyboard()
        initRecyclerView()
        binding.createGroupPhoto.setOnClickListener { addPhoto()  }
        binding.createGroupBtnComplete.setOnClickListener {
            val nameGroup = binding.createGroupInputName.text.toString()
            if (nameGroup.isEmpty()){
                showToast("Введіте імя")
            } else {
                createGroupToDatabase(nameGroup,mUri,listContacts){
                    replaceFragment(MainListFragment())
                }
            }
        }
        binding.createGroupInputName.requestFocus()
        binding.createGroupCounts.text = getPlurals(listContacts.size)
    }

    fun addPhoto(){

            ImagePicker.with(this)
                .crop() //Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(250, 250)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK || requestCode == ImagePicker.REQUEST_CODE && data != null) {

            val resultUri: Uri? = data?.data!!
            binding.createGroupPhoto.setImageURI(mUri)

        }else if (resultCode == ImagePicker.RESULT_ERROR) {

        }
    }

    private fun initRecyclerView() {
        mRecyclerView = binding.createGroupRecycleView
        mAdapter = AddContactsAdapter()
        mRecyclerView.adapter = mAdapter
        listContacts.forEach {  mAdapter.updateListItems(it) }
    }
}