package donets.danylo.android.utasapp.ui.screens.groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.database.CURRENT_UID
import donets.danylo.android.utasapp.database.NODE_MAIN_LIST
import donets.danylo.android.utasapp.database.NODE_MESSAGES
import donets.danylo.android.utasapp.database.NODE_PHONES_CONTACTS
import donets.danylo.android.utasapp.database.NODE_USERS
import donets.danylo.android.utasapp.database.REF_DATABASE_ROOT
import donets.danylo.android.utasapp.database.getCommonModel
import donets.danylo.android.utasapp.databinding.FragmentAddContactsBinding
import donets.danylo.android.utasapp.models.CommonModel
import donets.danylo.android.utasapp.ui.screens.base.BaseFragment
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY
import donets.danylo.android.utasapp.utilits.AppValueEventListener
import donets.danylo.android.utasapp.utilits.hideKeyboard
import donets.danylo.android.utasapp.utilits.replaceFragment
import donets.danylo.android.utasapp.utilits.showToast

class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private val mRefContactsList = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()
    private var _binding: FragmentAddContactsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentAddContactsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onResume() {
        listContacts.clear()
        super.onResume()
        APP_ACTIVITY.title = "Додати учасника"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
        initRecyclerView()
        binding.addContactsBtnNext.setOnClickListener {
            if (listContacts.isEmpty()) showToast("Додайте учасника")
            else replaceFragment(CreateGroupFragment(listContacts))

        }
    }

    private fun initRecyclerView() {
        mRecyclerView = binding.addContactsRecycleView
        mAdapter = AddContactsAdapter()

        // 1 запрос
        mRefContactsList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach { model ->

                // 2 запрос
                mRefUsers.child(model.id)
                    .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                        val newModel = dataSnapshot1.getCommonModel()

                        // 3 запрос
                        mRefMessages.child(model.id).limitToLast(1)
                            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                                val tempList = dataSnapshot2.children.map { it.getCommonModel() }

                                if (tempList.isEmpty()){
                                    newModel.lastMessage = "Чат очищено"
                                } else {
                                    newModel.lastMessage = tempList[0].text
                                }


                                if (newModel.fullname.isEmpty()) {
                                    newModel.fullname = newModel.phone
                                }
                                mAdapter.updateListItems(newModel)
                            })
                    })
            }
        })

        mRecyclerView.adapter = mAdapter
    }

    companion object{
        val listContacts = mutableListOf<CommonModel>()
    }
}