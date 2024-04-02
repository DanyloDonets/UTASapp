package donets.danylo.android.utasapp.ui.messageRecyclerView.viewHolder

import donets.danylo.android.utasapp.ui.messageRecyclerView.views.MessageView

interface MessageHolder {
    fun drawMessage(view: MessageView)
    fun onAttach(view: MessageView)
    fun onDettach()
}