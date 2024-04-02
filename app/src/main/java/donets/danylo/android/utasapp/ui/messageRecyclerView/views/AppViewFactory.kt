package donets.danylo.android.utasapp.ui.messageRecyclerView.views

import android.R
import donets.danylo.android.utasapp.models.CommonModel
import donets.danylo.android.utasapp.utilits.TYPE_MESSAGE_FILE
import donets.danylo.android.utasapp.utilits.TYPE_MESSAGE_IMAGE
import donets.danylo.android.utasapp.utilits.TYPE_MESSAGE_VOICE


class AppViewFactory {
    companion object{
        fun getView(message: CommonModel): MessageView {
            return when(message.type){
                TYPE_MESSAGE_IMAGE -> ViewImageMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl
                )

                TYPE_MESSAGE_VOICE ->
                    ViewVoiceMessage(
                        message.id,
                        message.from,
                        message.timeStamp.toString(),
                        message.fileUrl
                    )

                TYPE_MESSAGE_FILE ->
                    ViewFileMessage(
                        message.id,
                        message.from,
                        message.timeStamp.toString(),
                        message.fileUrl,
                        message.text
                    )


                else -> ViewTextMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.text
                )
            }
        }
    }
}