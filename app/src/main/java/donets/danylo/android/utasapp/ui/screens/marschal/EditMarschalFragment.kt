package donets.danylo.android.utasapp.ui.screens.marschal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.database.sqlDbFuns
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY


class EditMarschalFragment(val marshalId:Int, val Name: String,val Phone:String,val Rating:String) : Fragment() {






        private lateinit var nameInput: EditText
        private lateinit var phoneInput: EditText
        private lateinit var ratingInput: EditText
        private lateinit var updateButton: Button
        private lateinit var deleteButton: Button

        private var id: String = ""
        private var name: String = ""
        private var phone: String = ""
        private var rating: String = ""

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_edit_marschal, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            nameInput = view.findViewById(R.id.name_marschal_input)
            phoneInput = view.findViewById(R.id.phone_input)
            ratingInput = view.findViewById(R.id.rating_input)
            updateButton = view.findViewById(R.id.update_button)
            deleteButton = view.findViewById(R.id.delete_button)

            getAndSetIntentData()

            // Set actionbar title after getAndSetIntentData method
            val ab: androidx.appcompat.app.ActionBar? = (activity as AppCompatActivity?)?.supportActionBar
            ab?.title = Name

            updateButton.setOnClickListener {
                val myDB = sqlDbFuns(requireActivity())
                name = nameInput.text.toString().trim()
                phone = phoneInput.text.toString().trim()
                rating = ratingInput.text.toString().trim()
                myDB.updateData(marshalId, name, phone, rating)
            }

            deleteButton.setOnClickListener {
                confirmDialog()
            }
        }

    private fun getAndSetIntentData() {

            // Setting Intent Data
            nameInput.setText(Name)
            phoneInput.setText(Phone)
            ratingInput.setText(Rating)
            Log.d("stev", "$Name $Phone $Rating")

    }




        private fun confirmDialog() {
            val builder = AlertDialog.Builder(APP_ACTIVITY)
            builder.setTitle("Delete $Name ?")
            builder.setMessage("Are you sure you want to delete $Name ?")
            builder.setPositiveButton("Yes") { dialogInterface, i ->
                val myDB = sqlDbFuns(requireActivity())
                myDB.deleteOneRow(marshalId)
                activity?.finish()
            }
            builder.setNegativeButton("No") { dialogInterface, i ->
                // Do nothing
            }
            builder.create().show()
        }
}
