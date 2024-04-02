package donets.danylo.android.utasapp.ui.screens.singleChat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import donets.danylo.android.utasapp.database.CURRENT_UID
import donets.danylo.android.utasapp.ui.messageRecyclerView.viewHolder.AppHolderFactory
import donets.danylo.android.utasapp.ui.messageRecyclerView.viewHolder.HolderImageMessage
import donets.danylo.android.utasapp.ui.messageRecyclerView.viewHolder.HolderTextMessage
import donets.danylo.android.utasapp.ui.messageRecyclerView.viewHolder.HolderVoiceMessage
import donets.danylo.android.utasapp.ui.messageRecyclerView.viewHolder.MessageHolder
import donets.danylo.android.utasapp.ui.messageRecyclerView.views.MessageView
import donets.danylo.android.utasapp.utilits.asTime
import donets.danylo.android.utasapp.utilits.downloadAndSetImage

@Suppress("DEPRECATION")
class SingleChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCache = mutableListOf<MessageView>()
    private var mListHolders = mutableListOf<MessageHolder>()

    override fun getItemViewType(position: Int): Int {
        return mListMessagesCache[position].getTypeView()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageHolder).drawMessage((mListMessagesCache[position]))
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onAttach(mListMessagesCache[holder.adapterPosition])
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onDettach()
        mListHolders.remove((holder as MessageHolder))
        super.onViewDetachedFromWindow(holder)
    }


    private fun drawMessageImage(holder: HolderImageMessage, position: Int) {


        if (mListMessagesCache[position].from == CURRENT_UID) {
            holder.blocReceivedImageMessage.visibility = View.GONE
            holder.blocUserImageMessage.visibility = View.VISIBLE
            holder.chatUserImage.downloadAndSetImage(mListMessagesCache[position].fileUrl)
            holder.chatUserImageMessageTime.text =
                mListMessagesCache[position].timeStamp.asTime()
        } else {
            holder.blocReceivedImageMessage.visibility = View.VISIBLE
            holder.blocUserImageMessage.visibility = View.GONE
            holder.chatReceivedImage.downloadAndSetImage(mListMessagesCache[position].fileUrl)
            holder.chatReceivedImageMessageTime.text =
                mListMessagesCache[position].timeStamp.asTime()
        }
    }

    private fun drawMessageText(holder: HolderTextMessage, position: Int) {

        if (mListMessagesCache[position].from == CURRENT_UID) {
            holder.blocUserMessage.visibility = View.VISIBLE
            holder.blocReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = mListMessagesCache[position].text
            holder.chatUserMessageTime.text =
                mListMessagesCache[position].timeStamp.asTime()
        } else {
            holder.blocUserMessage.visibility = View.GONE
            holder.blocReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = mListMessagesCache[position].text
            holder.chatReceivedMessageTime.text =
                mListMessagesCache[position].timeStamp.asTime()
        }
    }


    fun addItemToBottom(
        item: MessageView,
        onSuccess: () -> Unit
    ) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            notifyItemInserted(mListMessagesCache.size)
        }
        onSuccess()
    }

    fun addItemToTop(
        item: MessageView,
        onSuccess: () -> Unit
    ) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            mListMessagesCache.sortBy { it.timeStamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }

    fun onDestroy() {
        mListHolders.forEach {
            it.onDettach()
        }
    }
}