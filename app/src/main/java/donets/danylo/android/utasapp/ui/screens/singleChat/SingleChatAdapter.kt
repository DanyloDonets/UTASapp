package donets.danylo.android.utasapp.ui.screens.singleChat

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.database.CURRENT_UID
import donets.danylo.android.utasapp.database.deleteMessage
import donets.danylo.android.utasapp.database.editMessage
import donets.danylo.android.utasapp.models.CommonModel
import donets.danylo.android.utasapp.ui.messageRecyclerView.viewHolder.AppHolderFactory
import donets.danylo.android.utasapp.ui.messageRecyclerView.viewHolder.MessageHolder
import donets.danylo.android.utasapp.ui.messageRecyclerView.views.MessageView
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY
import donets.danylo.android.utasapp.utilits.showToast

@Suppress("DEPRECATION")
class SingleChatAdapter(var contact: CommonModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCache = mutableListOf<MessageView>()
    private var mListHolders = mutableListOf<MessageHolder>()




    override fun getItemViewType(position: Int): Int {
        return mListMessagesCache[position].getTypeView()
    }

    private fun showEditTextDialog(messageText:String,id:String, position: Int){

        val builder = AlertDialog.Builder(APP_ACTIVITY, R.style.AlertDialogTheme)
        val inflater = LayoutInflater.from(APP_ACTIVITY)
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.et_editText)
        editText.setText(messageText)

        with(builder){
            setTitle("Змінити повідомлення")
            setPositiveButton("Ok"){dialog, witch ->
                editMessage( editText.text.toString(),id, contact.id)
            mListMessagesCache[position].text = editText.text.toString()}
            setView(dialogLayout)
            show()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = AppHolderFactory.getHolder(parent, viewType)

        holder.itemView.setOnClickListener {
            if(CURRENT_UID == mListMessagesCache[holder.adapterPosition].from){
            AlertDialog.Builder(APP_ACTIVITY, R.style.AlertDialogTheme)
                .setTitle("Обріть дію")
                .setMessage("Оберіть дію")

                .setPositiveButton("Видалити повідомлення")
                { _,_ ->
                    deleteMessage(mListMessagesCache[holder.adapterPosition].id, contact.id)
                    showToast("Повідомлення видалене")
                    Log.i("loging", "delete")
                    mListMessagesCache.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)

                }.setNegativeButton("Змінити повідомлення")
                { _,_ ->
                    if (mListMessagesCache[holder.adapterPosition].fileUrl == "empty"){
                        showEditTextDialog(mListMessagesCache[holder.adapterPosition].text,mListMessagesCache[holder.adapterPosition].id, holder.adapterPosition)
                        notifyItemChanged(holder.adapterPosition)
                    }else{
                        showToast("Це повідомлення неможливо змінити")
                    }


                }
                .setCancelable(true)
                .show()

        }}
        return holder
    }

    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }*/

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