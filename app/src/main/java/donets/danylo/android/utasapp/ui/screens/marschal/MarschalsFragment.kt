package donets.danylo.android.utasapp.ui.screens.marschal


import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.recreate
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.database.sqlDbFuns

import donets.danylo.android.utasapp.databinding.FragmentMarschalsBinding
import donets.danylo.android.utasapp.ui.screens.singleChat.SingleChatAdapter
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY
import donets.danylo.android.utasapp.utilits.replaceFragment


class MarschalsFragment : Fragment() {

    private var _binding: FragmentMarschalsBinding? = null
    private val binding get() = _binding!!
    private lateinit var myDB: sqlDbFuns
    private lateinit var marsschalId: ArrayList<Int>
    private lateinit var marschalName: ArrayList<String>
    private lateinit var marschalPhone: ArrayList<String>
    private lateinit var marshalRating: ArrayList<String>
    private lateinit var customAdapter: MarsschalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarschalsBinding.inflate(inflater, container, false)
        //sqlDbFuns(APP_ACTIVITY).syncWithSheets()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            replaceFragment(AddMarschalFragment(),true)
        }

        binding.search.addTextChangedListener {
            storeDataInArrays(binding.search.text.toString())
            binding.recyclerView.adapter?.notifyDataSetChanged()
            customAdapter = MarsschalAdapter(requireActivity(), this, marsschalId, marschalName, marschalPhone, marshalRating)
            binding.recyclerView.adapter = customAdapter
        }

        myDB = sqlDbFuns(requireActivity())
        marsschalId = ArrayList()
        marschalName = ArrayList()
        marschalPhone = ArrayList()
        marshalRating = ArrayList()

        storeDataInArrays(binding.search.text.toString())

        customAdapter = MarsschalAdapter(requireActivity(), this, marsschalId, marschalName, marschalPhone, marshalRating)
        binding.recyclerView.adapter = customAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
       // myDB.syncWithSheets()
        _binding = null
    }

    private fun storeDataInArrays(text:String) {

        val cursor: Cursor?
        if(text =="") {
            cursor =
                myDB.readAllData()
        }else{ cursor = myDB.searchData(text)}
        if (cursor?.count == 0) {
            binding.emptyImageview.visibility = View.VISIBLE
            binding.noData.visibility = View.VISIBLE
        } else {
            while (cursor!!.moveToNext()) {
                marsschalId.add(cursor.getInt(0)) // Assuming the first column is an integer
                marschalName.add(cursor.getString(1))
                marschalPhone.add(cursor.getString(2))
                marshalRating.add(cursor.getString(3))
            }
            cursor.close() // Don't forget to close the cursor when done
            binding.emptyImageview.visibility = View.GONE
            binding.noData.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all) {
            confirmDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete All?")
        builder.setMessage("Are you sure you want to delete all Data?")
        builder.setPositiveButton("Yes") { dialogInterface, i ->
            val myDB = sqlDbFuns(requireActivity())
            myDB.deleteAllData()
            replaceFragment(MarschalsFragment(), false)
        }
        builder.setNegativeButton("No") { dialogInterface, i ->
            // Do nothing
        }
        builder.create().show()
    }
}
