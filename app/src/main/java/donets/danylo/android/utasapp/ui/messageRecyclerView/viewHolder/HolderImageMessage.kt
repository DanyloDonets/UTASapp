package donets.danylo.android.utasapp.ui.messageRecyclerView.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.database.CURRENT_UID
import donets.danylo.android.utasapp.ui.messageRecyclerView.views.MessageView
import donets.danylo.android.utasapp.utilits.asTime
import donets.danylo.android.utasapp.utilits.downloadAndSetImage


class HolderImageMessage(view: View): RecyclerView.ViewHolder(view), MessageHolder {
    val blocReceivedImageMessage: ConstraintLayout = view.findViewById(R.id.bloc_received_image_message)
    val blocUserImageMessage: ConstraintLayout = view.findViewById(R.id.bloc_user_image_message)
    val chatUserImage: ImageView = view.findViewById(R.id.chat_user_image)
    val chatReceivedImage: ImageView = view.findViewById(R.id.chat_received_image)
    val chatUserImageMessageTime: TextView = view.findViewById(R.id.chat_user_image_message_time)
    val chatReceivedImageMessageTime: TextView = view.findViewById(R.id.chat_received_image_message_time)



    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blocReceivedImageMessage.visibility = View.GONE
            blocUserImageMessage.visibility = View.VISIBLE
            chatUserImage.downloadAndSetImage(view.fileUrl)
            chatUserImageMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blocReceivedImageMessage.visibility = View.VISIBLE
            blocUserImageMessage.visibility = View.GONE
            chatReceivedImage.downloadAndSetImage(view.fileUrl)
            chatReceivedImageMessageTime.text =
                view.timeStamp.asTime()
        }

    }

    override fun onAttach(view: MessageView) {

    }

    override fun onDettach() {

    }
}