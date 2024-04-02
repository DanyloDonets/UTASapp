package donets.danylo.android.utasapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY
import donets.danylo.android.utasapp.utilits.AppValueEventListener
import donets.danylo.android.utasapp.database.CURRENT_UID
import donets.danylo.android.utasapp.database.NODE_PHONES_CONTACTS
import donets.danylo.android.utasapp.database.NODE_USERS
import donets.danylo.android.utasapp.database.REF_DATABASE_ROOT
import donets.danylo.android.utasapp.database.getCommonModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import donets.danylo.android.utasapp.databinding.ContactItemBinding
import donets.danylo.android.utasapp.databinding.FragmentContactsBinding
import donets.danylo.android.utasapp.models.CommonModel
import donets.danylo.android.utasapp.ui.fragments.singleChat.SingleChatFragment
import donets.danylo.android.utasapp.utilits.downloadAndSetImage
import donets.danylo.android.utasapp.utilits.replaceFragment

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts: DatabaseReference
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefUsersListener:AppValueEventListener
    private  var mapListeners = hashMapOf<DatabaseReference,AppValueEventListener>()

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Контакти"
        initRecycleView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
    }

    private fun initRecycleView() {
        mRecyclerView = binding.contactsRecycleView
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)
            .build()

        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ContactsHolder(binding)
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)

                mRefUsersListener = AppValueEventListener { dataSnapshot ->
                    val contact = dataSnapshot.getCommonModel()
                    if (contact.fullname.isEmpty()){
                        holder.name.text = model.fullname
                    } else holder.name.text = contact.fullname
                    holder.status.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photoUrl)
                    holder.itemView.setOnClickListener { replaceFragment(SingleChatFragment(model)) }                }
                mRefUsers.addValueEventListener(mRefUsersListener)
                mapListeners[mRefUsers] = mRefUsersListener
            }
        }

        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        mapListeners.forEach {
            it.key.removeEventListener(it.value)
        }
    }



    class ContactsHolder(private val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.contactFullname1
        val status: TextView = binding.contactStatus
        val photo: CircleImageView = binding.contactPhoto
    }


}