package donets.danylo.android.utasapp.ui.messageRecyclerView.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.database.CURRENT_UID
import donets.danylo.android.utasapp.ui.messageRecyclerView.views.MessageView
import donets.danylo.android.utasapp.utilits.AppVoicePlayer
import donets.danylo.android.utasapp.utilits.asTime

class HolderVoiceMessage(view: View): RecyclerView.ViewHolder(view),MessageHolder {

    private val mAppVoicePlayer = AppVoicePlayer()
    val blocReceivedVoiceMessage: ConstraintLayout = view.findViewById(R.id.bloc_received_voice_message)
    val blocUserVoiceMessage: ConstraintLayout = view.findViewById(R.id.bloc_user_voice_message)
    val chatUserVoiceMessageTime: TextView = view.findViewById(R.id.chat_user_voice_message_time)
    val chatReceivedVoiceMessageTime: TextView = view.findViewById(R.id.chat_received_voice_message_time)


    val chatReceivedBtnPlay: ImageView = view.findViewById(R.id.chat_received_btn_play)
    val chatReceivedBtnStop:ImageView = view.findViewById(R.id.chat_received_btn_stop)

    val chatUserBtnPlay:ImageView = view.findViewById(R.id.chat_user_btn_play)
    val chatUserBtnStop:ImageView = view.findViewById(R.id.chat_user_btn_stop)




    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blocReceivedVoiceMessage.visibility = View.GONE
            blocUserVoiceMessage.visibility = View.VISIBLE
            chatUserVoiceMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blocReceivedVoiceMessage.visibility = View.VISIBLE
            blocUserVoiceMessage.visibility = View.GONE
            chatReceivedVoiceMessageTime.text =
                view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {
        mAppVoicePlayer.init()
        if (view.from == CURRENT_UID) {
            chatUserBtnPlay.setOnClickListener {
                chatUserBtnPlay.visibility = View.GONE
                chatUserBtnStop.visibility = View.VISIBLE
                chatUserBtnStop.setOnClickListener {
                    stop {
                        chatUserBtnStop.setOnClickListener(null)
                        chatUserBtnPlay.visibility = View.VISIBLE
                        chatUserBtnStop.visibility = View.GONE
                    }
                }
                play(view) {
                    chatUserBtnPlay.visibility = View.VISIBLE
                    chatUserBtnStop.visibility = View.GONE
                }
            }
        } else {
            chatReceivedBtnPlay.setOnClickListener {
                chatReceivedBtnPlay.visibility = View.GONE
                chatReceivedBtnStop.visibility = View.VISIBLE
                chatReceivedBtnStop.setOnClickListener {
                    stop {
                        chatReceivedBtnStop.setOnClickListener(null)
                        chatReceivedBtnPlay.visibility = View.VISIBLE
                        chatReceivedBtnStop.visibility = View.GONE
                    }
                }
                play(view) {
                    chatReceivedBtnPlay.visibility = View.VISIBLE
                    chatReceivedBtnStop.visibility = View.GONE
                }
            }
        }
    }

    private fun play(
        view: MessageView,
        function: () -> Unit
    ) {
        mAppVoicePlayer.play(view.id, view.fileUrl) {
            function()
        }
    }

    fun stop(function: () -> Unit) {
        mAppVoicePlayer.stop {
            function()
        }
    }

    override fun onDettach() {
        chatReceivedBtnPlay.setOnClickListener(null)
        chatUserBtnPlay.setOnClickListener(null)
        mAppVoicePlayer.release()
    }

}