package donets.danylo.android.utasapp.ui.fragments

import android.os.Bundle
import android.view.*
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.database.CHILD_BIO
import donets.danylo.android.utasapp.database.CURRENT_UID
import donets.danylo.android.utasapp.database.NODE_USERS
import donets.danylo.android.utasapp.database.REF_DATABASE_ROOT
import donets.danylo.android.utasapp.database.USER
import donets.danylo.android.utasapp.database.setBioToDatabase
import donets.danylo.android.utasapp.databinding.FragmentChangeBioBinding
import donets.danylo.android.utasapp.utilits.*

@Suppress("DEPRECATION")
class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    private var _binding: FragmentChangeBioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChangeBioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.settingsInputBio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = binding.settingsInputBio.text.toString()
        setBioToDatabase(newBio)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
