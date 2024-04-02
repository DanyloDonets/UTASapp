package donets.danylo.android.utasapp.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.*
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.databinding.FragmentChangeNameBinding
import donets.danylo.android.utasapp.database.USER
import donets.danylo.android.utasapp.database.setNameToDatabase
import donets.danylo.android.utasapp.utilits.showToast

@Suppress("DEPRECATION")
class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    private var _binding: FragmentChangeNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeNameBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val fullnameList = USER.fullname.split(" ")
        if(fullnameList.size >1){
            binding.settingsInputSurname.setText(fullnameList[1])
        }
        binding.settingsInputName.setText(fullnameList[0])

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.e("Loging", "clicked to change")
        when (item.itemId) {

            R.id.settings_confirm_change -> changeName()
        }
        return true
    }

    private fun changeName() {
        Log.e("Loging", "on function")
        val name = binding.settingsInputName.text.toString()
        val surname = binding.settingsInputSurname.text.toString()
        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullName = "$name $surname"

            setNameToDatabase(fullName)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
