package donets.danylo.android.utasapp.ui.screens.settings

import android.os.Bundle
import android.view.*
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.database.USER
import donets.danylo.android.utasapp.database.setBioToDatabase
import donets.danylo.android.utasapp.databinding.FragmentChangeBioBinding
import donets.danylo.android.utasapp.ui.screens.base.BaseChangeFragment

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
