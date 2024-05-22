package donets.danylo.android.utasapp.ui.screens.marschal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.database.sqlDbFuns
import donets.danylo.android.utasapp.utilits.replaceFragment


class AddMarschalFragment : Fragment() {


        private lateinit var nameInput: EditText
        private lateinit var phoneInput: EditText
        private lateinit var ratingInput: EditText
        private lateinit var addButton: Button

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_add_marschal, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            nameInput = view.findViewById(R.id.name_marschal_input)
            phoneInput = view.findViewById(R.id.phone_input)
            ratingInput = view.findViewById(R.id.rating_input)
            addButton = view.findViewById(R.id.add_button)

            addButton.setOnClickListener {
                val myDB = sqlDbFuns(requireActivity())
                myDB.addMarschal(
                    nameInput.text.toString().trim(),
                    phoneInput.text.toString().trim(),
                    ratingInput.text.toString().trim()
                )
                replaceFragment(MarschalsFragment(),false)
            }
        }
    }
