package donets.danylo.android.utasapp.ui.fragments.singleChat

import android.Manifest.permission.RECORD_AUDIO
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AbsListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.databinding.FragmentSingleChatBinding
import donets.danylo.android.utasapp.models.CommonModel
import donets.danylo.android.utasapp.models.UserModel
import donets.danylo.android.utasapp.ui.fragments.BaseFragment
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY
import donets.danylo.android.utasapp.utilits.AppValueEventListener
import donets.danylo.android.utasapp.database.CURRENT_UID

import donets.danylo.android.utasapp.database.FOLDER_PROFILE_IMAGE
import donets.danylo.android.utasapp.database.NODE_MESSAGES
import donets.danylo.android.utasapp.database.NODE_USERS
import donets.danylo.android.utasapp.database.REF_DATABASE_ROOT
import donets.danylo.android.utasapp.database.REF_STORAGE_ROOT
import donets.danylo.android.utasapp.database.TYPE_TEXT
import donets.danylo.android.utasapp.database.USER
import donets.danylo.android.utasapp.utilits.downloadAndSetImage
import donets.danylo.android.utasapp.database.getCommonModel
import donets.danylo.android.utasapp.database.getMessageKey
import donets.danylo.android.utasapp.database.getUrlFromStorage
import donets.danylo.android.utasapp.database.getUserModel
import donets.danylo.android.utasapp.database.putFileToStorage

import donets.danylo.android.utasapp.database.putUrlToDatabase
import donets.danylo.android.utasapp.database.sendMessage

import donets.danylo.android.utasapp.database.uploadFileToStorage
import donets.danylo.android.utasapp.utilits.AppChildEventListener
import donets.danylo.android.utasapp.utilits.AppTextWatcher
import donets.danylo.android.utasapp.utilits.AppVoiceRecorder
import donets.danylo.android.utasapp.utilits.checkPermission
import donets.danylo.android.utasapp.utilits.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import donets.danylo.android.utasapp.utilits.RECORD_AUDIO
import donets.danylo.android.utasapp.utilits.TYPE_MESSAGE_IMAGE
import donets.danylo.android.utasapp.utilits.TYPE_MESSAGE_VOICE

@Suppress("DEPRECATION")
class SingleChatFragment(contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!
    private val contact_ = contact

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo: Toolbar
    private lateinit var mRefUser: DatabaseReference
    private lateinit var toolbarInfoLayout: ConstraintLayout
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppChildEventListener
    private var mCountMessages = 3
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAppVoiceRecorder: AppVoiceRecorder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSingleChatBinding.bind(view)}

    private fun initRecycleView() {
        mRecyclerView = binding.chatRecycleView
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES)
            .child(CURRENT_UID)
            .child(contact_.id)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.layoutManager = mLayoutManager
        mMessagesListener = AppChildEventListener{
            val message = it.getCommonModel()

            if (mSmoothScrollToPosition) {
                mAdapter.addItemToBottom(message) {
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }
            } else {
                mAdapter.addItemToTop(message) {
                    mSwipeRefreshLayout.isRefreshing = false
                }
            }
        }
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)


        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 3){
                    updateData()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }
        })
        mSwipeRefreshLayout.setOnRefreshListener { updateData() }
    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessagesListener)
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)

    }

    private fun initToolbar() {
        mToolbarInfo = APP_ACTIVITY.findViewById<Toolbar>(R.id.main_toolbar)
        toolbarInfoLayout = mToolbarInfo.findViewById<ConstraintLayout>(R.id.toolbar_info)
        toolbarInfoLayout.visibility = View.VISIBLE
        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoToolbar()
        }

        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact_.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)
        binding.chatBtnSendMessage.setOnClickListener{
            mSmoothScrollToPosition = true
            val message = binding.chatInputMessage.text.toString()
            if(message.isEmpty()){
                showToast("Введіть повідомлення")
            }else{
                sendMessage(message, contact_.id, TYPE_TEXT){
                    binding.chatInputMessage.setText("")
                }
            }
        }
    }

    private fun initInfoToolbar() {
        if (mReceivingUser.fullname.isEmpty()) {
            toolbarInfoLayout.findViewById<TextView>(R.id.toolbar_chat_fullname).text = contact_.fullname
        } else  toolbarInfoLayout.findViewById<TextView>(R.id.toolbar_chat_fullname).text = mReceivingUser.fullname

        toolbarInfoLayout.findViewById<CircleImageView>(R.id.toolbar_chat_image).downloadAndSetImage(mReceivingUser.photoUrl)
        toolbarInfoLayout.findViewById<TextView>(R.id.contact_chat_status).text = mReceivingUser.status
    }

    override fun onResume() {
        super.onResume()
        initFields()
        initToolbar()
        initRecycleView()
        toolbarInfoLayout.visibility = View.VISIBLE
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initFields() {
        mSwipeRefreshLayout = binding.chatSwipeRefresh
        mLayoutManager = LinearLayoutManager(this.context)
        mAppVoiceRecorder = AppVoiceRecorder()
        binding.chatInputMessage.addTextChangedListener(AppTextWatcher {
            val string = binding.chatInputMessage.text.toString()
            if (string.isEmpty() || string == "Запис") {
                binding.chatBtnSendMessage.visibility = View.GONE
                binding.chatBtnAttach.visibility = View.VISIBLE
                binding.chatBtnVoice.visibility = View.VISIBLE
            } else {
                binding.chatBtnSendMessage.visibility = View.VISIBLE
                binding.chatBtnAttach.visibility = View.GONE
                binding.chatBtnVoice.visibility = View.GONE
            }
        })

        binding.chatBtnAttach.setOnClickListener { attachFile() }

        CoroutineScope(Dispatchers.IO).launch {
            binding.chatBtnVoice.setOnTouchListener { v, event ->
                if (checkPermission(donets.danylo.android.utasapp.utilits.RECORD_AUDIO)){
                    if (event.action == MotionEvent.ACTION_DOWN){
                        //TODO record
                        binding.chatInputMessage.setText("Запис")
                       binding.chatBtnVoice.setColorFilter(
                            ContextCompat.getColor(
                                APP_ACTIVITY,
                                R.color.colorPrimary
                            )
                        )
                        val messageKey = getMessageKey(contact_.id)
                        mAppVoiceRecorder.startRecord(messageKey)
                    } else if (event.action == MotionEvent.ACTION_UP) {
                        //TODO stop record
                        binding.chatInputMessage.setText("")
                        binding.chatBtnVoice.colorFilter = null
                        mAppVoiceRecorder.stopRecord { file, messageKey ->
                            uploadFileToStorage(Uri.fromFile(file), messageKey,contact_.id, TYPE_MESSAGE_VOICE)
                            mSmoothScrollToPosition = true
                        }
                    }
                }
                true
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri = data?.data
            val messageKey = getMessageKey(contact_.id)

            if (imageUri != null) {
                uploadFileToStorage(imageUri, messageKey, contact_.id, TYPE_MESSAGE_IMAGE)
            }
            mSmoothScrollToPosition = true

        }
    }


    private fun attachFile() {
        ImagePicker.with(this)
            .crop() //Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(250, 250)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()}

    override fun onPause() {
        super.onPause()
        toolbarInfoLayout.visibility = View.GONE
        mRefMessages.removeEventListener(mMessagesListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAppVoiceRecorder.releaseRecorder()
    }
}