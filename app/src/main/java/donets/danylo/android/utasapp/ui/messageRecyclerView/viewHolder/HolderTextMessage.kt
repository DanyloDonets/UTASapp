package donets.danylo.android.utasapp.ui.messageRecyclerView.viewHolder

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.database.CURRENT_UID
import donets.danylo.android.utasapp.ui.messageRecyclerView.views.MessageView
import donets.danylo.android.utasapp.utilits.asTime

class HolderTextMessage(view: View): RecyclerView.ViewHolder(view),MessageHolder {
    val blocUserMessage: ConstraintLayout = view.findViewById(R.id.bloc_user_message)
    val chatUserMessage: TextView = view.findViewById(R.id.chat_user_message)
    val chatUserMessageTime: TextView = view.findViewById(R.id.chat_user_message_time)
    val blocReceivedMessage: ConstraintLayout = view.findViewById(R.id.bloc_received_message)
    val chatReceivedMessage: TextView = view.findViewById(R.id.chat_received_message)
    val chatReceivedMessageTime: TextView = view.findViewById(R.id.chat_received_message_time)




    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blocUserMessage.visibility = View.VISIBLE
            blocReceivedMessage.visibility = View.GONE
            chatUserMessage.text = view.text
            chatUserMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blocUserMessage.visibility = View.GONE
            blocReceivedMessage.visibility = View.VISIBLE
            chatReceivedMessage.text = view.text
            chatReceivedMessageTime.text =
                view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {

    }

    override fun onDettach() {

    }


}